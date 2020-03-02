/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import base.BaseSpec
import connectors.SecureCommsAlertConnector
import models.{ErrorModel, GenericParsingError, JsonParsingError}
import models.responseModels.SecureCommsResponseModel
import org.scalamock.scalatest.MockFactory
import utils.SecureCommsMessageBodyStrings
import utils.SecureCommsMessageTestData.Responses.expectedResponseDeRegistration

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

class SecureCommsAlertServiceSpec extends BaseSpec with MockFactory {

  val mockConnector: SecureCommsAlertConnector = mock[SecureCommsAlertConnector]
  val service: SecureCommsAlertService = new SecureCommsAlertService(mockConnector)

  val serviceName = "doesn't matter"
  val regNum = "someTypeOfNumberAndLetters"
  val communicationsId = "129480912840912380912"
  val dateToUse: String = "2019-01-01T09:00:00Z"

  "getSecureCommsMessage" must {
    "return a successful response" when {
      "the response can be correctly parsed into a SecureCommsMessageModel" in {
        (mockConnector.getSecureCommsMessage(_: String, _: String, _: String)(_: ExecutionContext))
            .expects(serviceName, regNum, communicationsId, *)
            .returns(
              Right(SecureCommsResponseModel(
                dateToUse,
                SecureCommsMessageBodyStrings.validEDODString
              ))
            )

        val result = await(service.getSecureCommsMessage(serviceName, regNum, communicationsId))
        result shouldBe Right(expectedResponseDeRegistration)
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
