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

import helpers.IntegrationBaseSpec
import metrics.QueueMetrics
import models.SecureCommsMessageModel
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport
import uk.gov.hmrc.mongo.workitem.WorkItem
import repositories.SecureMessageQueueRepository
import testutils.TestModels.secureCommsModel

class SecureMessageServiceISpec extends IntegrationBaseSpec with
  DefaultPlayMongoRepositorySupport[WorkItem[SecureCommsMessageModel]]{

  override def beforeAll(): Unit = super.beforeAll()
  override def afterAll(): Unit = super.afterAll()

  override lazy val repository: SecureMessageQueueRepository = new SecureMessageQueueRepository(appConfig, mongoComponent)

  lazy val secureCommsService: SecureCommsService = mock[SecureCommsService]
  lazy val metrics: QueueMetrics = mock[QueueMetrics]
  lazy val service = new SecureMessageService(repository, secureCommsService, metrics)

  "CommsEventService.retrieveWorkItems" when {

    "there is an item in the database" should {

      "remove the item" in {

        await(service.queueRequest(secureCommsModel))
        await(repository.pullOutstanding).size shouldBe 1

        await(service.retrieveWorkItems)
        await(repository.pullOutstanding).size shouldBe 0

      }

    }

  }

}
