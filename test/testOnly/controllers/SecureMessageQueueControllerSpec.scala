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

package testOnly.controllers

import base.BaseSpec
import models.SecureCommsMessageModel
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.Result
import play.api.test.Helpers.{await, contentAsString, defaultAwaitTimeout}
import repositories.SecureMessageQueueRepository
import services.SecureMessageQueuePollingService
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport
import uk.gov.hmrc.mongo.workitem.WorkItem
import utils.SecureCommsMessageTestData.Responses.expectedResponseEverything

import scala.concurrent.Future

class SecureMessageQueueControllerSpec extends BaseSpec with MockitoSugar with
  DefaultPlayMongoRepositorySupport[WorkItem[SecureCommsMessageModel]] {

  override lazy val repository = new SecureMessageQueueRepository(mockAppConfig, mongoComponent)
  val scheduler: SecureMessageQueuePollingService = mock[SecureMessageQueuePollingService]
  val controller = new SecureMessageQueueController(repository, scheduler, cc)

  "The count action" should {

    "return the total number of records in the SecureMessageQueue database" in {
      await(repository.pushNew(expectedResponseEverything))
      await(repository.pushNew(expectedResponseEverything))
      lazy val result: Future[Result] = controller.count(request)

      contentAsString(result) shouldBe "2"
    }
  }
}
