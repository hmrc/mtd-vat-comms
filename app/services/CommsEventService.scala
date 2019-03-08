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
import uk.gov.hmrc.workitem.Failed
import uk.gov.hmrc.workitem.WorkItem
import utils.LoggerUtil.logError

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

@Singleton
class CommsEventService @Inject()(commsEventQueueRepository: CommsEventQueueRepository,
                                  secureCommsAlertService: SecureCommsAlertService,
                                  emailMessageService: EmailMessageService,
                                  secureMessageService: SecureMessageService,
                                  metrics: QueueMetrics)(
                                  implicit ec: ExecutionContext) {

  def queueRequest(item: VatChangeEvent): Future[Boolean] = {
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
          if (model.transactorDetails.transactorEmail.nonEmpty | model.originalEmailAddress.getOrElse("").nonEmpty) {
            emailMessageService.queueRequest(model).flatMap {
              case true =>
                secureMessageService.queueRequest(model)
                metrics.commsEventDequeued()
                commsEventQueueRepository.complete(workItem.id).map(_ => acc)
              case false =>
                commsEventQueueRepository.markAs(workItem.id, Failed, None).map(_ => acc)
            }
          } else {
            secureMessageService.queueRequest(model)
            metrics.commsEventDequeued()
            commsEventQueueRepository.complete(workItem.id).map(_ => acc)
          }
        case Left(GenericParsingError) | Left(JsonParsingError) | Left(NotFoundNoMatch) | Left(BadRequest) =>
          metrics.commsEventDequeued()
          commsEventQueueRepository.complete(workItem.id).map(_ => acc)
        case Left(_) =>
          commsEventQueueRepository.markAs(workItem.id, Failed, None).map(_ => acc)
      }
    } catch {
      case e: Throwable =>
        logError(content = s"[CommsEventService][processWorkItem] - Unexpected Error recovered.", e)
        metrics.commsEventDequeued()
        commsEventQueueRepository.complete(workItem.id).map(_ => acc)
    }
  }
}
