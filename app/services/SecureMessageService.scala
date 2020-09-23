/*
 * Copyright 2020 HM Revenue & Customs
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

import javax.inject.Inject
import metrics.QueueMetrics
import models._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import repositories.SecureMessageQueueRepository
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem.{Failed, PermanentlyFailed, WorkItem}
import utils.LoggerUtil.{logError, logWarn}

import scala.concurrent.{ExecutionContext, Future}

class SecureMessageService @Inject()(secureMessageQueueRepository: SecureMessageQueueRepository,
                                     secureCommsService: SecureCommsService,
                                     metrics: QueueMetrics)(
                                     implicit ec: ExecutionContext) {

  def queueRequest(item: SecureCommsMessageModel): Future[Boolean] = {
    metrics.secureMessageEnqueued()
    secureMessageQueueRepository.pushNew(item, DateTimeUtils.now).map(_ => true)
  }

  def retrieveWorkItems: Future[Seq[SecureCommsMessageModel]] = {
    val pullWorkItems: Enumerator[WorkItem[SecureCommsMessageModel]] =
      Enumerator.generateM(secureMessageQueueRepository.pullOutstanding)

    val processWorkItems = Iteratee.foldM(Seq.empty[SecureCommsMessageModel]) {
      processWorkItem
    }

    pullWorkItems.run(processWorkItems)
  }

  def processWorkItem(acc: Seq[SecureCommsMessageModel], workItem: WorkItem[SecureCommsMessageModel]): Future[Seq[SecureCommsMessageModel]] = {

    try {

      val secureCommsModel: Future[Either[ErrorModel, Boolean]] =
        secureCommsService.sendSecureCommsMessage(workItem.item)
      secureCommsModel.flatMap {
        case Right(_) =>
          metrics.secureMessageDequeued()
          secureMessageQueueRepository.complete(workItem.id).map(_ => acc)
        case Left(GenericQueueNoRetryError) =>
          metrics.secureMessageGenericQueueNoRetryError()
          handleNonRecoverableError(acc, workItem, "GenericQueueNoRetryError")
        case Left(BadRequest) =>
          metrics.secureMessageBadRequestError()
          handleNonRecoverableError(acc, workItem, "BadRequestError")
        case Left(SpecificParsingError) =>
          metrics.secureMessageSpecificParsingError()
          handleNonRecoverableError(acc, workItem, "SpecificParsingError")
        case Left(errorModel) =>
          logWarn("[SecureMessageService][processWorkItem] - Unexpected error received - " +
            s"Code: ${errorModel.code}, Body: ${errorModel.body}")
          metrics.secureMessageQueuedForRetry()
          secureMessageQueueRepository.markAs(workItem.id, Failed, None).map(_ => acc)
      }
    } catch {
      case e: Throwable =>
        metrics.secureMessageUnexpectedError()
        handleNonRecoverableError(acc, workItem, "UnexpectedError", Some(e))
    }
  }.recoverWith {
    case e =>
      metrics.secureMessageUnexpectedError()
      handleNonRecoverableError(acc, workItem, "UnexpectedError recoverWith", Some(e))
  }

  private def handleNonRecoverableError(acc: Seq[SecureCommsMessageModel], workItem: WorkItem[SecureCommsMessageModel],
                                  errorTypeName: String, exception: Option[Throwable] = None): Future[Seq[SecureCommsMessageModel]] = {

    val message = s"[SecureMessageService][processWorkItem] - $errorTypeName when processing item with vrn: " +
      s"${workItem.item.vrn}, form bundle ref: ${workItem.item.formBundleReference} and work item id: ${workItem.id}"

    exception match {
      case Some(error) => logError(message, error)
      case None => logWarn(message)
    }

    secureMessageQueueRepository.markAs(workItem.id, PermanentlyFailed, None).map(_ => acc)
  }
}
