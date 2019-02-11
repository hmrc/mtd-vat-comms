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
import models._
import models.emailRendererModels.EmailRendererRequestModel
import models.responseModels.EmailRendererResponseModel
import play.api.http.Status._
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.WSClient
import testutils.WireMockHelper

import scala.concurrent.ExecutionContext.Implicits.global

class EmailRendererConnectorIT extends IntegrationBaseSpec with WireMockHelper {

  val wsClient: WSClient = app.injector.instanceOf(classOf[WSClient])
  val appConfig: AppConfig = app.injector.instanceOf(classOf[AppConfig])

  val connector: EmailRendererConnector = new EmailRendererConnector(wsClient, appConfig)

  val postUrl: String = "/hmrc/email"

  "sendEmailRequest" should {
    "return a EmailRendererResponseModel" when {
      s"an $ACCEPTED response is received from EmailRenderer" in {
        val postBody = EmailRendererRequestModel(
          Seq("fusrohdah@whiterun.tam"),
          "thisIsDefoAnId",
          Map(
            "FUS" -> "ROH DAH"
          )
        )
        val apiResponse: JsObject = Json.obj()

        stubPostRequest(
          postUrl,
          Json.toJson(postBody),
          ACCEPTED,
          apiResponse
        )

        val expectedResult = Right(EmailRendererResponseModel(ACCEPTED))

        val result: Either[ErrorModel, EmailRendererResponseModel] = await(connector.sendEmailRequest(
          EmailRendererRequestModel(
            Seq("fusrohdah@whiterun.tam"),
            "thisIsDefoAnId",
            Map(
              "FUS" -> "ROH DAH"
            )
          )
        ))

        result shouldBe expectedResult
      }
    }
    "return an ErrorModel" when {
      s"a $BAD_REQUEST response is received from SecureComms, and the response can be parsed" in {
        val postBody = EmailRendererRequestModel(
          Seq("fusrohdah@whiterun.tam"),
          "thisIsDefoAnId",
          Map(
            "FUS" -> "ROH DAHL"
          )
        )
        val apiResponse: JsObject = Json.obj(
          "error" -> "Something weird happened"
        )

        stubPostRequest(
          postUrl,
          Json.toJson(postBody),
          BAD_REQUEST,
          apiResponse
        )

        val expectedResult = Left(ErrorModel(BAD_REQUEST.toString, Json.stringify(apiResponse)))

        val result: Either[ErrorModel, EmailRendererResponseModel] = await(connector.sendEmailRequest(
          EmailRendererRequestModel(
            Seq("fusrohdah@whiterun.tam"),
            "thisIsDefoAnId",
            Map(
              "FUS" -> "ROH DAHL"
            )
          )
        ))

        result shouldBe expectedResult
      }
    }
  }
}
