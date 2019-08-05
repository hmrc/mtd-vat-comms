/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import common.ApiConstants.serviceName
import javax.inject.{Inject, Singleton}
import metrics.QueueMetrics
import models._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import repositories.CommsEventQueueRepository
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem.{Failed, PermanentlyFailed, WorkItem}
import utils.LoggerUtil.{logDebug, logError, logWarn}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommsEventService @Inject()(commsEventQueueRepository: CommsEventQueueRepository,
                                  secureCommsAlertService: SecureCommsAlertService,
                                  emailMessageService: EmailMessageService,
                                  secureMessageService: SecureMessageService,
                                  metrics: QueueMetrics)(
                                  implicit ec: ExecutionContext) {

  def queueRequest(item: VatChangeEvent): Future[Boolean] = {
    logDebug(s"[CommsEventService][queueRequest] - Item queued: $item")
    metrics.commsEventEnqueued()
    commsEventQueueRepository.pushNew(item, DateTimeUtils.now).map(_ => true)
  }

  def retrieveWorkItems(implicit ec: ExecutionContext): Future[Seq[VatChangeEvent]] = {
    val pullWorkItems: Enumerator[WorkItem[VatChangeEvent]] =
      Enumerator.generateM(commsEventQueueRepository.pullOutstanding)

    val processWorkItems = Iteratee.foldM(Seq.empty[VatChangeEvent]) {
      processWorkItem
    }

    pullWorkItems.run(processWorkItems)
  }

  def processWorkItem(acc: Seq[VatChangeEvent], workItem: WorkItem[VatChangeEvent]): Future[Seq[VatChangeEvent]] = {

    try {
      val secureCommsModel =
        secureCommsAlertService.getSecureCommsMessage(serviceName, workItem.item.vrn, workItem.item.BPContactNumber)
      secureCommsModel.flatMap {
        case Right(model) =>
          handleItemSuccess(acc, workItem, model)
        case Left(GenericParsingError) =>
          metrics.commsEventGenericParsingError()
          handleNonRecoverableError(acc, workItem, "GenericParsingError")
        case Left(JsonParsingError) =>
          metrics.commsEventJsonParsingError()
          handleNonRecoverableError(acc, workItem, "JsonParsingError")
        case Left(NotFoundNoMatch) =>
          metrics.commsEventNotFoundError()
          handleNonRecoverableError(acc, workItem, "NotFoundError")
        case Left(BadRequest) =>
          metrics.commsEventBadRequestError()
          handleNonRecoverableError(acc, workItem, "BadRequestError")
        case Left(_) =>
          metrics.commsEventQueuedForRetry()
          commsEventQueueRepository.markAs(workItem.id, Failed, None).map(_ => acc)
      }
    } catch {
      case e: Throwable =>
        metrics.commsEventUnexpectedError()
        handleNonRecoverableError(acc, workItem, "UnexpectedError", Some(e))
    }
  }.recoverWith {
    case e =>
      metrics.commsEventUnexpectedError()
      handleNonRecoverableError(acc, workItem, "UnexpectedError recoverWith", Some(e))
  }

  private def handleItemSuccess(acc: Seq[VatChangeEvent], workItem: WorkItem[VatChangeEvent], model: SecureCommsMessageModel): Future[Seq[VatChangeEvent]] = {
    if (model.transactorDetails.transactorEmail.nonEmpty | model.originalEmailAddress.getOrElse("").nonEmpty) {
      emailMessageService.queueRequest(model).flatMap {
        case true =>
          secureMessageService.queueRequest(model)
          metrics.commsEventDequeued()
          commsEventQueueRepository.complete(workItem.id).map(_ => acc)
        case false =>
          metrics.commsEventQueuedForRetry()
          commsEventQueueRepository.markAs(workItem.id, Failed, None).map(_ => acc)
      }
    } else {
      secureMessageService.queueRequest(model)
      metrics.commsEventDequeued()
      commsEventQueueRepository.complete(workItem.id).map(_ => acc)
    }
  }

  private def handleNonRecoverableError(acc: Seq[VatChangeEvent], workItem: WorkItem[VatChangeEvent],
                                        errorTypeName: String, exception: Option[Throwable] = None): Future[Seq[VatChangeEvent]] = {

    val message = s"[CommsEventService][processWorkItem] - $errorTypeName when processing item with vrn: " +
      s"${workItem.item.vrn}, BPContactNumber: ${workItem.item.BPContactNumber} and work item id: ${workItem.id}"

    exception match {
      case Some(error) => logError(message, error)
      case None => logWarn(message)
    }

    commsEventQueueRepository.markAs(workItem.id, PermanentlyFailed, None).map(_ => acc)
  }
}
