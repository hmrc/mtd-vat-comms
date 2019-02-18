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

import javax.inject.Inject
import metrics.QueueMetrics
import models.{ErrorModel, SecureCommsMessageModel}
import play.api.libs.iteratee.{Enumerator, Iteratee}
import repositories.SecureMessageQueueRepository
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem.{Failed, WorkItem}

import scala.concurrent.{ExecutionContext, Future}

class SecureMessageService @Inject()(secureMessageQueueRepository: SecureMessageQueueRepository,
                                     secureCommsService: SecureCommsService,
                                     metrics: QueueMetrics)(
                                      implicit ec: ExecutionContext) {

  def queueRequest(item: SecureCommsMessageModel): Future[Boolean] =
    secureMessageQueueRepository.pushNew(item, DateTimeUtils.now).map(_ => true)

  def retrieveWorkItems(implicit ec: ExecutionContext): Future[Seq[SecureCommsMessageModel]] = {
    val pullWorkItems: Enumerator[WorkItem[SecureCommsMessageModel]] =
      Enumerator.generateM(secureMessageQueueRepository.pullOutstanding)

    val processWorkItems = Iteratee.foldM(Seq.empty[SecureCommsMessageModel]) {
      processWorkItem
    }

    pullWorkItems.run(processWorkItems)
  }

  def processWorkItem(acc: Seq[SecureCommsMessageModel], workItem: WorkItem[SecureCommsMessageModel]): Future[Seq[SecureCommsMessageModel]] = {
    val secureCommsModel: Future[Either[ErrorModel, Boolean]] =
      secureCommsService.sendSecureCommsMessage(workItem.item)
    secureCommsModel.flatMap {
      case Right(_) =>
        metrics.commsEventDequeued()
        secureMessageQueueRepository.complete(workItem.id).map(_ => acc)
      case Left(errorModel) if errorModel.code == "ERROR_PARSING_RESPONSE" =>
        // was 201 so created but could not parse the response
        metrics.commsEventDequeued()
        secureMessageQueueRepository.complete(workItem.id).map(_ => acc)
      case Left(_) =>
        // some other response that wasn't a created
        secureMessageQueueRepository.markAs(workItem.id, Failed, None).map(_ => acc)
    }
  }
}