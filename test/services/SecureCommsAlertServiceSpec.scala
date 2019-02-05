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

package services

import base.BaseSpec
import connectors.SecureCommsAlertConnector
import models.{ErrorModel, GenericParsingError, JsonParsingError}
import models.responseModels.SecureCommsResponseModel
import models.secureCommsModels.messageTypes._
import org.joda.time.DateTime
import org.scalamock.scalatest.MockFactory
import utils.SecureCommsMessageBodyStrings

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

class SecureCommsAlertServiceSpec extends BaseSpec with MockFactory {

  val mockConnector: SecureCommsAlertConnector = mock[SecureCommsAlertConnector]
  val service: SecureCommsAlertService = new SecureCommsAlertService(mockConnector)

  val serviceName = "doesn't matter"
  val regNum = "someTypeOfNumberAndLetters"
  val communicationsId = "129480912840912380912"
  val dateToUse: DateTime = DateTime.now()

  "getSecureCommsMessage" must {
    "return a successful response" when {
      "the response can be correctly parsed into a DeRegistrationModel" in {
        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
            .expects(serviceName, regNum, communicationsId, *)
            .returns(
              Right(SecureCommsResponseModel(
                dateToUse,
                SecureCommsMessageBodyStrings.validEDODString
              ))
            )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result.right.get.getClass.getSimpleName + "$" shouldBe DeRegistrationModel.getClass.getSimpleName
      }
      "the response can be correctly parsed into a PPOBChangeModel" in {
        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
            .expects(serviceName, regNum, communicationsId, *)
            .returns(
              Right(SecureCommsResponseModel(
                dateToUse,
                SecureCommsMessageBodyStrings.validPPOBChangeString
              ))
            )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result.right.get.getClass.getSimpleName + "$" shouldBe PPOBChangeModel.getClass.getSimpleName
      }
      "the response can be correctly parsed into a RepaymentsBankAccountChangeModel" in {
        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
            .expects(serviceName, regNum, communicationsId, *)
            .returns(
              Right(SecureCommsResponseModel(
                dateToUse,
                SecureCommsMessageBodyStrings.validBankAccountChangeString
              ))
            )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result.right.get.getClass.getSimpleName + "$" shouldBe RepaymentsBankAccountChangeModel.getClass.getSimpleName
      }
      "the response can be correctly parsed into a VATStaggerChangeModel" in {
        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
            .expects(serviceName, regNum, communicationsId, *)
            .returns(
              Right(SecureCommsResponseModel(
                dateToUse,
                SecureCommsMessageBodyStrings.validStaggerChangeString
              ))
            )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result.right.get.getClass.getSimpleName + "$" shouldBe VATStaggerChangeModel.getClass.getSimpleName
      }
      "the response can be correctly parsed into a EmailAddressChangeModel" in {
        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
            .expects(serviceName, regNum, communicationsId, *)
            .returns(
              Right(SecureCommsResponseModel(
                dateToUse,
                SecureCommsMessageBodyStrings.validEmailChangeString
              ))
            )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result.right.get.getClass.getSimpleName + "$" shouldBe EmailAddressChangeModel.getClass.getSimpleName
      }
      "the response can be correctly parsed into a BusinessNameChangeModel" in {
        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
            .expects(serviceName, regNum, communicationsId, *)
            .returns(
              Right(SecureCommsResponseModel(
                dateToUse,
                SecureCommsMessageBodyStrings.validBusinessNameChangeString
              ))
            )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result.right.get.getClass.getSimpleName + "$" shouldBe BusinessNameChangeModel.getClass.getSimpleName
      }
    }
    "return an error" when {
      "the provided string parses into Json, but not into a generic model" in {
        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
          .expects(serviceName, regNum, communicationsId, *)
          .returns(
            Right(SecureCommsResponseModel(
              dateToUse,
              SecureCommsMessageBodyStrings.invalidEDODString
            ))
          )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result shouldBe Left(GenericParsingError)
      }
      "the provided string cannot be parsed into Json" in {
        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
          .expects(serviceName, regNum, communicationsId, *)
          .returns(
            Right(SecureCommsResponseModel(
              dateToUse,
              SecureCommsMessageBodyStrings.invalidJsonParseString
            ))
          )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result shouldBe Left(JsonParsingError)
      }
      "the connector returns an error" in {
        val modelToReturn = ErrorModel("SOME_CODE", "SOME_ERROR_TEXT")

        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
          .expects(serviceName, regNum, communicationsId, *)
          .returns(
            Left(modelToReturn)
          )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result shouldBe Left(modelToReturn)
      }
    }
  }
}
