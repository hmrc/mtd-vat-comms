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

package testOnly.controllers

import base.BaseSpec
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import play.api.mvc.Result
import repositories.SecureMessageQueueRepository

import scala.concurrent.Future

class SecureMessageQueueControllerSpec extends BaseSpec with MockitoSugar {

  val repository: SecureMessageQueueRepository = mock[SecureMessageQueueRepository]
  val controller = new SecureMessageQueueController(repository)
  val recordCount = 99

  "The count action" should {

    "return the total number of records in the SecureMessageQueue database" in {
      when(repository.count(ec)) thenReturn Future.successful(recordCount)
      lazy val result: Future[Result] = controller.count(request)

      await(bodyOf(result)) shouldBe recordCount.toString
    }
  }
}
