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
import connectors.SecureCommsServiceConnector
import models.responseModels.SecureCommsServiceResponseModel
import models.secureCommsServiceModels._
import models._
import org.scalamock.scalatest.MockFactory
import play.api.i18n.MessagesApi
import utils.SecureCommsMessageTestData.SendSecureMessageModels._
import scala.concurrent.ExecutionContext
import common.Constants.MessageKeys._

class SecureCommsServiceSpec extends BaseSpec with MockFactory {

  val mockConnector: SecureCommsServiceConnector = mock[SecureCommsServiceConnector]
  implicit lazy val messages: MessagesApi = injector.instanceOf[MessagesApi]
  val service: SecureCommsService = new SecureCommsService(mockConnector)

  val serviceName = "doesn't matter"
  val regNum = "someTypeOfNumberAndLetters"
  val communicationsId = "129480912840912380912"
  val dateToUse: String = "2019-01-01T09:00:00Z"

  "getSecureCommsMessage" must {

    "return a successful response for a transactor approved deRegistration" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(deRegistrationValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a transactor rejected deRegistration" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(deRegistrationValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client approved deRegistration" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(deRegistrationValidApprovedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client rejected deRegistration" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(deRegistrationValidRejectedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a transactor bank details approval" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(bankDetailsValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a transactor bank details rejection" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(bankDetailsValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client bank details approval" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(bankDetailsValidApprovedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client bank details rejection" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(bankDetailsValidRejectedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a transactor stagger approval" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a transactor stagger rejection" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client stagger approval" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerValidApprovedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client stagger rejection" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerValidRejectedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a transactor ppob approval" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(ppobValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a transactor ppob rejection" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(ppobValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client ppob approval" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(ppobValidApprovedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client ppob rejection" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(ppobValidRejectedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client email approval" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(emailValidApprovedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return a successful response for a client email rejection" when {
      "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(emailValidRejectedClientRequest))
        result shouldBe Right(true)
      }
    }

    "return an error when there is an invalid template id" when {
      "the response can be correctly parsed into a SecureCommsMessageModel" in {
        val result = await(service.sendSecureCommsMessage(messageModelDeRegistrationInvalidTemplate))
        result shouldBe Left(GenericQueueNoRetryError)
      }
    }

    "return an error" when {
      "an exception is encountered" in {
        (mockConnector.sendMessage(_: SecureCommsServiceRequestModel)(_: ExecutionContext))
          .expects(*, *)
          .returns(
            Left(BadRequestUnknownTaxIdentifier)
          )

        val result = await(service.sendSecureCommsMessage(emailValidRejectedClientRequest))
        result shouldBe Left(BadRequestUnknownTaxIdentifier)
      }
    }

    "return a GenericQueueNoRetryError error" when {
      "an unknown MessageModel is passed to build a request" in {
        val result = await(service.buildResponse(unsupportedMessageModel, isTransactor = true, isApproval = true))
        result shouldBe Left(GenericQueueNoRetryError)
      }
    }

    "return the expected subject for an email secure message" when {
      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = EMAIL_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "You have successfully changed your email address for VAT"
      }
    }

    "return the expected subject for an email secure message" when {
      "it is for a client rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = EMAIL_BASE_KEY, isApproval = false, isTransactor = false)
        result shouldBe "We have rejected the change of email address for VAT"
      }
    }

    "return the expected subject for an ppob secure message" when {
      "it is for a transactor approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = PPOB_BASE_KEY, isApproval = true, isTransactor = true)
        result shouldBe "Your agent has successfully changed your principal place of business for VAT"
      }
    }

    "return the expected subject for an ppob secure message" when {
      "it is for a transactor rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = PPOB_BASE_KEY, isApproval = false, isTransactor = true)
        result shouldBe "We have rejected your agent’s change to your principal place of business for VAT"
      }
    }

    "return the expected subject for an ppob secure message" when {
      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = PPOB_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "You have successfully changed your principal place of business for VAT"
      }
    }

    "return the expected subject for an ppob secure message" when {
      "it is for a client rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = PPOB_BASE_KEY, isApproval = false, isTransactor = false)
        result shouldBe "We have rejected the change to your principal place of business for VAT"
      }
    }

    "return the expected subject for a stagger secure message" when {
      "it is for a transactor approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = STAGGER_BASE_KEY, isApproval = true, isTransactor = true)
        result shouldBe "Your agent has successfully changed your VAT Return dates"
      }
    }

    "return the expected subject for a stagger secure message" when {
      "it is for a transactor rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = STAGGER_BASE_KEY, isApproval = false, isTransactor = true)
        result shouldBe "We have rejected your agent’s change to your business VAT Return dates"
      }
    }

    "return the expected subject for a stagger secure message" when {
      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = STAGGER_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "You have successfully changed your VAT Return dates"
      }
    }

    "return the expected subject for a stagger secure message" when {
      "it is for a client rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = STAGGER_BASE_KEY, isApproval = false, isTransactor = false)
        result shouldBe "We have rejected the change to your business VAT Return dates"
      }
    }

    "return the expected subject for a deregister secure message" when {
      "it is for a transactor approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval = true, isTransactor = true)
        result shouldBe "We have accepted your agent’s request to deregister from VAT"
      }
    }

    "return the expected subject for a deregister secure message" when {
      "it is for a transactor rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval = false, isTransactor = true)
        result shouldBe "We have rejected your agent’s request to deregister your business from VAT"
      }
    }

    "return the expected subject for an deregister secure message" when {
      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "We have accepted your request to deregister from VAT"
      }
    }

    "return the expected subject for an deregister secure message" when {
      "it is for a client rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval = false, isTransactor = false)
        result shouldBe "We have rejected your request to deregister from VAT"
      }
    }

    "return the expected subject for a bank details secure message" when {
      "it is for a transactor approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval = true, isTransactor = true)
        result shouldBe "Your agent has successfully changed your bank details for VAT repayments"
      }
    }

    "return the expected subject for a bank details secure message" when {
      "it is for a transactor rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval = false, isTransactor = true)
        result shouldBe "We have rejected your agent’s change to your bank details for VAT repayments"
      }
    }

    "return the expected subject for an bank details secure message" when {
      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "You have successfully changed your bank details for VAT repayments"
      }
    }

    "return the expected subject for an bank details secure message" when {
      "it is for a client rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval = false, isTransactor = false)
        result shouldBe "We have rejected the change to your bank details for VAT repayments"
      }
    }
  }

  private def setupSuccessResponse = {
    (mockConnector.sendMessage(_: SecureCommsServiceRequestModel)(_: ExecutionContext))
      .expects(*, *)
      .returns(
        Right(SecureCommsServiceResponseModel(
          "1234"
        ))
      )
  }
}
