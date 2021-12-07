/*
 * Copyright 2021 HM Revenue & Customs
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
import models.{BadRequest, NotFoundNoMatch, SecureCommsMessageModel}
import play.api.libs.iteratee.{Enumerator, Iteratee}
import repositories.EmailMessageQueueRepository
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem.{Failed, PermanentlyFailed, WorkItem}
import utils.{LoggerUtil, SecureCommsMessageParser}

import scala.concurrent.{ExecutionContext, Future}

class EmailMessageService @Inject()(emailMessageQueueRepository: EmailMessageQueueRepository,
                                    emailService: EmailService,
                                    metrics: QueueMetrics)(
                                    implicit ec: ExecutionContext) extends LoggerUtil {

  def queueRequest(item: SecureCommsMessageModel): Future[Boolean] = {
    metrics.emailMessageEnqueued()
    emailMessageQueueRepository.pushNew(item, DateTimeUtils.now).map(_ => true)
  }

  def retrieveWorkItems: Future[Seq[SecureCommsMessageModel]] = {
    val pullWorkItems: Enumerator[WorkItem[SecureCommsMessageModel]] =
      Enumerator.generateM(emailMessageQueueRepository.pullOutstanding)

    val processWorkItems = Iteratee.foldM(Seq.empty[SecureCommsMessageModel]) {
      processWorkItem
    }

    pullWorkItems.run(processWorkItems)
  }

  def processWorkItem(acc: Seq[SecureCommsMessageModel],
                      workItem: WorkItem[SecureCommsMessageModel]): Future[Seq[SecureCommsMessageModel]] = {

      SecureCommsMessageParser.parseModel(workItem.item) match {
        case Right(message) => emailService.sendEmailRequest(message).flatMap {
          case Right(_) =>
            metrics.emailMessageDequeued()
            emailMessageQueueRepository.complete(workItem.id).map(_ => acc)
          case Left(BadRequest) =>
            metrics.emailMessageBadRequestError()
            handleNonRecoverableError(acc, workItem, "BadRequestError")
          case Left(NotFoundNoMatch) =>
            metrics.emailMessageNotFoundError()
            handleNonRecoverableError(acc, workItem, "MessageNotFoundError")
          case _ =>
            metrics.emailMessageQueuedForRetry()
            emailMessageQueueRepository.markAs(workItem.id, Failed, None).map(_ => acc)
        }
        case _ =>
          metrics.emailMessageParsingError()
          handleNonRecoverableError(acc, workItem, "ParsingError")
      }
  }.recoverWith {
    case e =>
      metrics.emailMessageUnexpectedError()
      handleNonRecoverableError(acc, workItem, "UnexpectedError recoverWith", Some(e))
  }

  private def handleNonRecoverableError(acc: Seq[SecureCommsMessageModel], workItem: WorkItem[SecureCommsMessageModel],
                                        errorTypeName: String, exception: Option[Throwable] = None): Future[Seq[SecureCommsMessageModel]] = {

    val message = s"[EmailMessageService][processWorkItem] - $errorTypeName when processing item with vrn: " +
      s"${workItem.item.vrn}, form bundle ref: ${workItem.item.formBundleReference} and work item id: ${workItem.id}"

    exception match {
      case Some(error) => logger.error(message, error)
      case None => logger.warn(message)
    }

    emailMessageQueueRepository.markAs(workItem.id, PermanentlyFailed, None).map(_ => acc)
  }
}
