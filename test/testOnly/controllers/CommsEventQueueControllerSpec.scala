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
import common.ApiConstants.vatChangeEventModel
import models.VatChangeEvent
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.Result
import play.api.test.Helpers.{await, contentAsString, defaultAwaitTimeout}
import repositories.CommsEventQueueRepository
import services.CommsEventQueuePollingService
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport
import uk.gov.hmrc.mongo.workitem.WorkItem

import scala.concurrent.Future

class CommsEventQueueControllerSpec extends BaseSpec with MockitoSugar with
  DefaultPlayMongoRepositorySupport[WorkItem[VatChangeEvent]]{

  override lazy val repository = new CommsEventQueueRepository(mockAppConfig, mongoComponent)
  val scheduler: CommsEventQueuePollingService = mock[CommsEventQueuePollingService]
  val controller = new CommsEventQueueController(repository, scheduler, cc)
  val vatChangeEvent: VatChangeEvent = vatChangeEventModel("PPOB Change")

  "The count action" should {

    "return the total number of records in the CommsEventQueue database" in {
      await(repository.pushNew(vatChangeEvent))
      await(repository.pushNew(vatChangeEvent))
      lazy val result: Future[Result] = controller.count(request)

      contentAsString(result) shouldBe "2"
    }
  }
}
