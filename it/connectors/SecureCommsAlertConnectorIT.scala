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

import config.{AppConfig, MicroserviceAppConfig}
import helpers.IntegrationBaseSpec
import models._
import models.responseModels.SecureCommsResponseModel
import org.joda.time.DateTime
import play.api.http.Status._
import play.api.libs.ws.WSClient
import testutils.WireMockHelper
import testutils.WireMockStubRequestBodies._

import scala.concurrent.ExecutionContext.Implicits.global

class SecureCommsAlertConnectorIT extends IntegrationBaseSpec with WireMockHelper {

  val wsClient: WSClient = app.injector.instanceOf(classOf[WSClient])
  val appConfig: AppConfig = app.injector.instanceOf(classOf[AppConfig])

  val connector: SecureCommsAlertConnector = new SecureCommsAlertConnector(wsClient, appConfig)

  val service: String = "value-added-tax"
  val regNum: String = "DD000000000"

  def generateUrl(communicationsId: String): String = {
    s"/secure-comms-alert/service/$service/registration-number/$regNum/communications/$communicationsId"
  }

  startWireMock()

  "getSecureComms" should {
    "return a SecureCommsResponseModel" when {
      s"an $OK response is received from SecureComms, and the response can be parsed" in {
        val dateTimeToUser = DateTime.now()
        val communicationId = "123456789011"

        stubGetRequest(
          generateUrl(communicationId),
          OK,
          secureCommsValidResponseEDOD(dateTimeToUser)
        )

        val expectedResult = SecureCommsResponseModel(
          dateTimeToUser,
          validEDODString
        )

        val result: Either[ErrorModel, SecureCommsResponseModel] = await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe Right(expectedResult)
      }
    }
    "return an ErrorModel" when {
      s"an $OK response is received from SecureComms, but the response cannot be parsed" in {
        val dateTimeToUser = DateTime.now()
        val communicationId = "123456789012"

        stubGetRequest(
          generateUrl(communicationId),
          OK,
          secureCommsInvalidResponseEDOD(dateTimeToUser)
        )

        val expectedResult = Left(UnableToParseSecureCommsResponseError)

        val result: Either[ErrorModel, SecureCommsResponseModel] = await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }
      s"a $NOT_FOUND is received from SecureComms" in {
        val communicationId = "123456789013"

        stubGetRequest(
          generateUrl(communicationId),
          NOT_FOUND,
          secureCommsValidErrorResponse("NOT_FOUND", "The back end has indicated that there is no match found.")
        )

        val expectedResult = Left(SecureCommsNotFoundError)

        val result: Either[ErrorModel, SecureCommsResponseModel] = await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }
      s"a $BAD_REQUEST response is received from SecureComms, and the response can be parsed" in {
        val communicationId = "123456789021"

        stubGetRequest(
          generateUrl(communicationId),
          BAD_REQUEST,
          secureCommsValidErrorResponse("INVALID_SERVICE", "Submission has not passed validation. Invalid Service")
        )

        val expectedResult = Left(ErrorModel("INVALID_SERVICE", "Submission has not passed validation. Invalid Service"))

        val result: Either[ErrorModel, SecureCommsResponseModel] = await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }
      s"a $BAD_REQUEST response is received from SecureComms, and the response cannot be parsed" in {
        val communicationId = "123456789021"

        stubGetRequest(
          generateUrl(communicationId),
          BAD_REQUEST,
          secureCommsInvalidErrorResponse("INVALID_SERVICE")
        )

        val expectedResult = Left(UnableToParseSecureCommsErrorResponseError)

        val result: Either[ErrorModel, SecureCommsResponseModel] = await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }
      s"an $INTERNAL_SERVER_ERROR response is received from SecureComms" in {
        val communicationId = "123456789014"

        stubGetRequest(
          generateUrl(communicationId),
          INTERNAL_SERVER_ERROR,
          secureCommsValidErrorResponse("INTERNAL_SERVER_ERROR", "This error message doesn't really matter.")
        )

        val expectedResult = Left(SecureCommsIssuesWithDesError)

        val result: Either[ErrorModel, SecureCommsResponseModel] = await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }
      s"a $SERVICE_UNAVAILABLE response is received from SecureComms" in {
        val communicationId = "123456789015"

        stubGetRequest(
          generateUrl(communicationId),
          SERVICE_UNAVAILABLE,
          secureCommsValidErrorResponse("SERVICE_UNAVAILABLE", "This error message doesn't really matter.")
        )

        val expectedResult = Left(SecureCommsServiceUnavailableError)

        val result: Either[ErrorModel, SecureCommsResponseModel] = await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }
      "an unexpected response is received from SecureComms" in {
        val communicationId = "123456789016"

        stubGetRequest(
          generateUrl(communicationId),
          REQUEST_TIMEOUT,
          ""
        )

        val expectedResult = Left(SecureCommsUnHandledResponseError)

        val result: Either[ErrorModel, SecureCommsResponseModel] = await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }
    }
  }

  stopWireMock()
}
