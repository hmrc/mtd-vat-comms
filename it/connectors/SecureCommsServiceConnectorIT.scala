/*
 * Copyright 2023 HM Revenue & Customs
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

package connectors

import helpers.IntegrationBaseSpec
import models._
import models.secureCommsServiceModels._
import play.api.http.Status._
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import testutils.WireMockHelper

class SecureCommsServiceConnectorIT extends IntegrationBaseSpec with WireMockHelper {

  val connector: SecureCommsServiceConnector = new SecureCommsServiceConnector(httpClient, appConfig)

  val requestModel: SecureCommsServiceRequestModel = SecureCommsServiceRequestModel(
    ExternalRefModel("anId"),
    RecipientModel(TaxIdentifierModel("AA", "06"), NameModel("first", Some("middle"), Some("last")),
      "dragon@born.tam"),
    "testMessageType",
    "testSubject",
    "testContent"
  )

  val url : String = "/messages"

  "sendMessage" should {

    "return a SecureCommsServiceResponseModel if the call is successful" in {
      val requestBody: JsValue = Json.toJson(requestModel)
      val returnBody: JsValue = Json.obj("id" -> "i_ajdsjhfkjshdf")

      stubPostRequest(url, requestBody, CREATED, returnBody)
      val result: Either[ErrorModel, Boolean] = await(connector.sendMessage(requestModel))

      result shouldBe Right(true)
    }

    "return an ErrorModel" when {

      "a BAD_REQUEST error is returned" in {
        val requestBody: JsValue = Json.toJson(requestModel)
        val returnBody: JsValue = Json.obj("reason" -> "this doesn't matter")

        stubPostRequest(url, requestBody, BAD_REQUEST, returnBody)
        val result: Either[ErrorModel, Boolean] = await(connector.sendMessage(requestModel))

        result shouldBe Left(BadRequest)
      }

      "a CONFLICT error is returned" in {
        val requestBody: JsValue = Json.toJson(requestModel)
        val returnBody: JsValue = Json.obj("reason" -> "this doesn't really matter")

        stubPostRequest(url, requestBody, CONFLICT, returnBody)
        val result = await(connector.sendMessage(requestModel))

        result shouldBe Left(ConflictDuplicateMessage)
      }

      "an unexpected response code is returned" in {
        val requestBody: JsValue = Json.toJson(requestModel)
        val returnBody: JsValue = Json.obj("reason" -> "some random reason")

        stubPostRequest(url, requestBody, NOT_IMPLEMENTED, returnBody)
        val result = await(connector.sendMessage(requestModel))

        result shouldBe Left(ErrorModel(s"${NOT_IMPLEMENTED}_RECEIVED_FROM_SERVICE", Json.stringify(returnBody)))
      }
    }
  }
}
