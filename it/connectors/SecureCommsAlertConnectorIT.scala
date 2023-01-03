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
import models.responseModels.SecureCommsResponseModel
import play.api.http.Status._
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import testutils.WireMockHelper
import testutils.WireMockStubRequestBodies._

class SecureCommsAlertConnectorIT extends IntegrationBaseSpec with WireMockHelper {

  val connector: SecureCommsAlertConnector = new SecureCommsAlertConnector(httpClient, appConfig)

  val service: String = "value-added-tax"
  val regNum: String = "DD000000000"
  val dateTimeToUser: String = "2019-01-01T09:00:00Z"

  def generateUrl(communicationsId: String): String =
    s"/secure-comms-alert/service/$service/registration-number/$regNum/communications/$communicationsId"

  "getSecureComms" should {

    "return a SecureCommsResponseModel" when {

      s"an $OK response is received from SecureComms, and the response can be parsed" in {

        val communicationId = "123456789011"

        stubGetRequest(
          generateUrl(communicationId),
          OK,
          secureCommsValidResponseEDOD(dateTimeToUser)
        )

        val expectedResult = SecureCommsResponseModel(dateTimeToUser, validEDODString)

        val result: Either[ErrorModel, SecureCommsResponseModel] =
          await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe Right(expectedResult)
      }
    }

    "return an ErrorModel" when {

      s"a $OK response is received from SecureComms, but the response cannot be parsed" in {

        val communicationId = "123456789012"

        stubGetRequest(
          generateUrl(communicationId),
          OK,
          secureCommsInvalidResponseEDOD(dateTimeToUser)
        )

        val expectedResult = Left(GenericParsingError)

        val result: Either[ErrorModel, SecureCommsResponseModel] =
          await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }

      s"a $BAD_REQUEST response is received from SecureComms" in {

        val communicationId = "123456789021"

        stubGetRequest(
          generateUrl(communicationId),
          BAD_REQUEST,
          secureCommsValidErrorResponse("INVALID_SERVICE", "Submission has not passed validation. Invalid Service")
        )

        val expectedResult = Left(BadRequest)

        val result: Either[ErrorModel, SecureCommsResponseModel] =
          await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }

      s"a $NOT_FOUND response is received from SecureComms" in {

        val communicationId = "123456789021"

        stubGetRequest(
          generateUrl(communicationId),
          NOT_FOUND,
          secureCommsValidErrorResponse("NOT_FOUND", "The back end has indicated that there is no match found.")
        )

        val expectedResult = Left(NotFoundNoMatch)

        val result: Either[ErrorModel, SecureCommsResponseModel] =
          await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }

      "an unexpected response is received from SecureComms" in {

        val communicationId = "123456789016"

        stubGetRequest(
          generateUrl(communicationId),
          REQUEST_TIMEOUT,
          "AN UNKNOWN ERROR HAS OCCURRED"
        )

        val expectedResult = Left(ErrorModel(s"$REQUEST_TIMEOUT", "AN UNKNOWN ERROR HAS OCCURRED"))

        val result: Either[ErrorModel, SecureCommsResponseModel] =
          await(connector.getSecureCommsMessage(service, regNum, communicationId))
        result shouldBe expectedResult
      }
    }
  }
}
