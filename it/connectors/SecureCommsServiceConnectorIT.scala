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

package connectors

import config.AppConfig
import helpers.IntegrationBaseSpec
import models.ErrorModel
import models.responseModels.SecureCommsServiceResponseModel
import models.secureCommsServiceModels._
import play.api.http.Status._
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import testutils.WireMockHelper

import scala.concurrent.ExecutionContext.Implicits.global

class SecureCommsServiceConnectorIT extends IntegrationBaseSpec with WireMockHelper {

  val wSClient: WSClient = app.injector.instanceOf(classOf[WSClient])
  val appConfig: AppConfig = app.injector.instanceOf(classOf[AppConfig])

  val connector: SecureCommsServiceConnector = new SecureCommsServiceConnector(wSClient, appConfig)

  val requestModel: SecureCommsServiceRequestModel = SecureCommsServiceRequestModel(
    ExternalRefModel("anId"),
    RecipientModel(TaxIdentifierModel("AA", "06"), NameModel("Fus", Some("Roh"), Some("Dah")),
      "dragon@born.tam"),
    "NOTICE",
    "ME",
    "SENPAI"
  )

  val url : String = "/messages"

  "sendMessage" should {
    "return a SecureCommsServiceResponseModel if the call is successful" in {
      val requestBody: JsValue = Json.toJson(requestModel)
      val returnBody: JsValue = Json.obj(
        "id" -> "i_ajdsjhfkjshdf"
      )

      stubPostRequest(url, requestBody, CREATED, returnBody)

      val result: Either[ErrorModel, SecureCommsServiceResponseModel] = await(connector.sendMessage(requestModel))

      result shouldBe Right(SecureCommsServiceResponseModel("i_ajdsjhfkjshdf"))
    }
    "return an ErrorModel" when {
      "the call is successful but the response cannot be " in {
        val requestBody: JsValue = Json.toJson(requestModel)
        val returnBody: JsValue = Json.obj(
          "didNotExpectThis" -> "i_ajdsjhfkjshdf"
        )

        stubPostRequest(url, requestBody, CREATED, returnBody)
        val result: Either[ErrorModel, SecureCommsServiceResponseModel] = await(connector.sendMessage(requestModel))

        result shouldBe Left(ErrorModel("ERROR_PARSING_RESPONSE", "There was an error parsing the response from the Secure Comms Service"))
      }
      "an unexpected response code is returned" in {
        val requestBody: JsValue = Json.toJson(requestModel)
        val returnBody: JsValue = Json.obj(
          "poipsaodifpoaispofia" -> "ashfljasdljfaskdj"
        )

        stubPostRequest(url, requestBody, NOT_FOUND, returnBody)
        val result = await(connector.sendMessage(requestModel))

        result shouldBe Left(ErrorModel("404_RECEIVED_FROM_SERVICE", Json.stringify(returnBody)))
      }
    }
  }
}
