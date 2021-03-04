/*
 * Copyright 2021 HM Revenue & Customs
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
import models.emailRendererModels.EmailRequestModel
import models.responseModels.EmailRendererResponseModel
import play.api.http.Status._
import play.api.libs.json.{JsObject, Json}
import testutils.WireMockHelper

class EmailConnectorIT extends IntegrationBaseSpec with WireMockHelper {

  val connector: EmailConnector = new EmailConnector(httpClient, appConfig)
  val postUrl: String = "/hmrc/email"
  val postBody = EmailRequestModel(
    Seq("test@email.com"),
    "testId",
    Map("key" -> "value")
  )

  "sendEmailRequest" should {

    "return a EmailRendererResponseModel" when {

      s"a $ACCEPTED response is received from EmailRenderer" in {

        val apiResponse: JsObject = Json.obj()

        stubPostRequest(postUrl, Json.toJson(postBody), ACCEPTED, apiResponse)

        val expectedResult = Right(EmailRendererResponseModel(ACCEPTED))
        val result: Either[ErrorModel, EmailRendererResponseModel] = await(connector.sendEmailRequest(postBody))

        result shouldBe expectedResult
      }
    }

    "return BadRequest" when {

      s"a $BAD_REQUEST response is received from EmailRenderer" in {

        val apiResponse: JsObject = Json.obj("body" -> "Bad request")

        stubPostRequest(postUrl, Json.toJson(postBody), BAD_REQUEST, apiResponse)

        val expectedResult = Left(BadRequest)
        val result: Either[ErrorModel, EmailRendererResponseModel] = await(connector.sendEmailRequest(postBody))

        result shouldBe expectedResult
      }
    }

    "return NotFoundNoMatch" when {

      s"a $NOT_FOUND response is received from EmailRenderer" in {

        val apiResponse: JsObject = Json.obj("body" -> "Not found")

        stubPostRequest(postUrl, Json.toJson(postBody), NOT_FOUND, apiResponse)

        val expectedResult = Left(NotFoundNoMatch)
        val result: Either[ErrorModel, EmailRendererResponseModel] = await(connector.sendEmailRequest(postBody))

        result shouldBe expectedResult
      }
    }

    "return an ErrorModel" when {

      s"an unexpected response is received from EmailRenderer" in {

        val apiResponse: JsObject = Json.obj("error" -> "Something weird happened")

        stubPostRequest(postUrl, Json.toJson(postBody), INTERNAL_SERVER_ERROR, apiResponse)

        val expectedResult = Left(ErrorModel(INTERNAL_SERVER_ERROR.toString, Json.stringify(apiResponse)))
        val result: Either[ErrorModel, EmailRendererResponseModel] = await(connector.sendEmailRequest(postBody))

        result shouldBe expectedResult
      }
    }
  }
}
