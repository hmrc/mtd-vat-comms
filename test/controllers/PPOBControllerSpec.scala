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

package controllers

import base.BaseSpec
import common.ApiConstants._
import common.VatChangeEventConstants._
import mocks.MockRepositoryAccessService
import models.VatChangeEvent
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NO_CONTENT}
import play.api.libs.json.JsObject
import play.api.mvc.Result

import scala.concurrent.Future

class PPOBControllerSpec extends BaseSpec with MockRepositoryAccessService {

  val controller = new PPOBController(mockRepoAccessService)

  val testRequestJson: JsObject        = vatChangeEventJson("PPOB Change")
  val testRequestModel: VatChangeEvent = vatChangeEventModel("PPOB Change")

  "The handleEvent action" when {

    "valid JSON is received" when {

      "the vat change event was successfully added to the queue" should {

        "return 204" in {
          mockQueueRequest(testRequestModel)(Future.successful(true))
          val result: Result = controller.handleEvent(request.withJsonBody(testRequestJson))

          status(result) shouldBe NO_CONTENT
        }
      }

      "the vat change event was unsuccessfully added to the queue" should {

        "return 500" in {
          mockQueueRequest(testRequestModel)(Future.successful(false))
          val result: Result = controller.handleEvent(request.withJsonBody(testRequestJson))

          status(result) shouldBe INTERNAL_SERVER_ERROR
        }
      }
    }

    "invalid JSON is received" should {

      "return 400" in {
        val result: Result = controller.handleEvent(request.withJsonBody(invalidJsonRequest))
        status(result) shouldBe BAD_REQUEST
      }

      "return JSON describing the error" in {
        val result: Result = controller.handleEvent(request.withJsonBody(invalidJsonRequest))
        jsonBodyOf(result) shouldBe invalidJsonResponse
      }
    }

    "something other than JSON is received" should {

      "return 400" in {
        val result: Result = controller.handleEvent(request.withBody(invalidRequestBody))
        status(result) shouldBe BAD_REQUEST
      }

      "return JSON describing the error" in {
        val result: Result = controller.handleEvent(request.withBody(invalidRequestBody))
        jsonBodyOf(result) shouldBe invalidFormatResponse
      }
    }
  }
}
