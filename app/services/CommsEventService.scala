/*
 * Copyright 2024 HM Revenue & Customs
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
import common.Constants.ChannelPreferences.DIGITAL
import metrics.QueueMetrics
import models._
import repositories.CommsEventQueueRepository
import uk.gov.hmrc.http.GatewayTimeoutException
import uk.gov.hmrc.mongo.workitem.WorkItem
import uk.gov.hmrc.mongo.workitem.ProcessingStatus._
import utils.{Enumerator, Iteratee, LoggerUtil}
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommsEventService @Inject()(commsEventQueueRepository: CommsEventQueueRepository,
                                  secureCommsAlertService: SecureCommsAlertService,
                                  emailMessageService: EmailMessageService,
                                  secureMessageService: SecureMessageService,
                                  metrics: QueueMetrics)(
                                  implicit ec: ExecutionContext) extends LoggerUtil {

  def queueRequest(item: VatChangeEvent): Future[Boolean] = {
    logger.debug(s"[CommsEventService][queueRequest] - Item queued: $item")
    metrics.commsEventEnqueued()
    commsEventQueueRepository.pushNew(item, commsEventQueueRepository.now()).map(_ => true)
  }

  def retrieveWorkItems: Future[Seq[VatChangeEvent]] = {
    val pullWorkItems: Enumerator[WorkItem[VatChangeEvent]] =
      Enumerator.generateM(commsEventQueueRepository.pullOutstanding)

    val processWorkItems = Iteratee.foldM(Seq.empty[VatChangeEvent]) {
      processWorkItem
    }

    pullWorkItems.run(processWorkItems)
  }

  def processWorkItem(acc: Seq[VatChangeEvent], workItem: WorkItem[VatChangeEvent]): Future[Seq[VatChangeEvent]] = {

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
    }.recoverWith {
      case _: GatewayTimeoutException =>
        metrics.commsEventQueuedForRetry()
        commsEventQueueRepository.markAs(workItem.id, Failed, None).map(_ => acc)
      case e =>
        metrics.commsEventUnexpectedError()
        handleNonRecoverableError(acc, workItem, "UnexpectedError recoverWith", Some(e))
    }
  }

  private def handleItemSuccess(acc: Seq[VatChangeEvent],
                                workItem: WorkItem[VatChangeEvent],
                                model: SecureCommsMessageModel): Future[Seq[VatChangeEvent]] =
    if (model.transactorDetails.transactorEmail.nonEmpty | model.originalEmailAddress.getOrElse("").nonEmpty) {
      emailMessageService.queueRequest(model).flatMap {
        case true =>
          if(model.preferences.channelPreference == DIGITAL) secureMessageService.queueRequest(model)
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

  private def handleNonRecoverableError(acc: Seq[VatChangeEvent], workItem: WorkItem[VatChangeEvent],
                                        errorTypeName: String, exception: Option[Throwable] = None): Future[Seq[VatChangeEvent]] = {

    val message = s"[CommsEventService][processWorkItem] - $errorTypeName when processing item with vrn: " +
      s"${workItem.item.vrn}, BPContactNumber: ${workItem.item.BPContactNumber} and work item id: ${workItem.id}"

    exception match {
      case Some(error) => logger.error(message, error)
      case None => logger.warn(message)
    }

    commsEventQueueRepository.markAs(workItem.id, PermanentlyFailed, None).map(_ => acc)
  }
}
