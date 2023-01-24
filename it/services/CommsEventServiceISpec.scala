/*
 * Copyright 2022 HM Revenue & Customs
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

import common.ApiConstants.vatChangeEventModel
import helpers.IntegrationBaseSpec
import metrics.QueueMetrics
import models.VatChangeEvent
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import repositories.CommsEventQueueRepository
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport
import uk.gov.hmrc.mongo.workitem.WorkItem

class CommsEventServiceISpec extends IntegrationBaseSpec with
  DefaultPlayMongoRepositorySupport[WorkItem[VatChangeEvent]] {

  override def beforeAll(): Unit = super.beforeAll()

  override def afterAll(): Unit = super.afterAll()

  override lazy val repository: CommsEventQueueRepository = new CommsEventQueueRepository(appConfig, mongoComponent)

  lazy val secureCommsService: SecureCommsAlertService = mock[SecureCommsAlertService]
  lazy val emailMessageService: EmailMessageService = mock[EmailMessageService]
  lazy val secureMessageService: SecureMessageService = mock[SecureMessageService]
  lazy val metrics: QueueMetrics = mock[QueueMetrics]
  lazy val service: CommsEventService = new CommsEventService(repository, secureCommsService, emailMessageService, secureMessageService, metrics)

  "CommsEventService.retrieveWorkItems" when {

    "there is an item in the database" should {

      val vatChangeEvent: VatChangeEvent = vatChangeEventModel("PPOB Change")

      "remove the item" in {

        await(service.queueRequest(vatChangeEvent))
        await(repository.pullOutstanding).size shouldBe 1

        await(service.retrieveWorkItems)
        await(repository.pullOutstanding).size shouldBe 0

      }

    }

  }
}
