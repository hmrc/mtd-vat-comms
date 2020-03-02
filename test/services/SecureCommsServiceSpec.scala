/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.format.DateTimeParseException

import base.BaseSpec
import common.Constants.MessageKeys._
import connectors.SecureCommsServiceConnector
import mocks.MockAppConfig
import models._
import models.secureCommsServiceModels._
import modules.SchedulerModule
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterAll
import play.api.Application
import play.api.i18n.MessagesApi
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import utils.SecureCommsMessageTestData.SendSecureMessageModels._

import scala.concurrent.ExecutionContext

class SecureCommsServiceSpec extends BaseSpec with MockFactory with BeforeAndAfterAll {

  val mockConnector: SecureCommsServiceConnector = mock[SecureCommsServiceConnector]

  implicit lazy val app: Application = new GuiceApplicationBuilder().disable[SchedulerModule].build
  val injector: Injector = app.injector
  implicit val mockAppConfig: MockAppConfig = new MockAppConfig(app.configuration)

  implicit val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]

  val service: SecureCommsService = new SecureCommsService(mockConnector)

  val serviceName = "doesn't matter"
  val regNum = "someTypeOfNumberAndLetters"
  val communicationsId = "129480912840912380912"
  val dateToUse: String = "2019-01-01T09:00:00Z"

  override def afterAll(): Unit = {
    app.stop()
    super.afterAll()
  }


  "getSecureCommsMessage" when {

    "the request can be correctly parsed into a SecureCommsMessageModel and the send succeeds" should {
      "return a successful response for a transactor approved deRegistration" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(deRegistrationValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor rejected deRegistration" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(deRegistrationValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client approved deRegistration" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(deRegistrationValidApprovedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client rejected deRegistration" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(deRegistrationValidRejectedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor bank details approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(bankDetailsValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor bank details rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(bankDetailsValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client bank details approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(bankDetailsValidApprovedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client bank details rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(bankDetailsValidRejectedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor stagger approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor stagger approval that had a annual accounting stagger code" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerLeaveAnnualAccountingValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor stagger rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client stagger approval that had a annual accounting stagger code" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerLeaveAnnualAccountingValidApprovedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client stagger approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerValidApprovedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client stagger rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(staggerValidRejectedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor ppob approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(ppobValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor ppob rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(ppobValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor's opt out approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(optOutRequestRepresented))
        result shouldBe Right(true)
      }

      "return a successful response for a client ppob approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(ppobValidApprovedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client ppob rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(ppobValidRejectedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client email approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(emailValidApprovedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client email rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(emailValidRejectedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a non represented user's opt out approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(optOutRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client website rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(websiteValidRejectedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client website approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(websiteValidApprovedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor website rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(websiteValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor website approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(websiteValidApprovedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client contact numbers rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(contactNumbersValidRejectedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a client contact numbers approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(contactNumbersValidApprovedClientRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor contact numbers rejection" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(contactNumbersValidRejectedTransactorRequest))
        result shouldBe Right(true)
      }

      "return a successful response for a transactor contact numbers approval" in {
        setupSuccessResponse

        val result = await(service.sendSecureCommsMessage(contactNumbersValidApprovedTransactorRequest))
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
          .returns(Left(BadRequest))

        val result = await(service.sendSecureCommsMessage(emailValidRejectedClientRequest))
        result shouldBe Left(BadRequest)
      }
    }

    "throw an exception" when {

      "an invalid stagger code is provided" in {
        intercept[MatchError](await(service.sendSecureCommsMessage(staggerInvalidCodeRequest)))
      }

      "an invalid date is provided" in {
        intercept[DateTimeParseException](await(service.sendSecureCommsMessage(staggerInvalidDatesRequest)))
      }
    }

    "return the expected subject for an email secure message" when {
      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = EMAIL_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "You have successfully changed your email address for VAT"
      }

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

      "it is for a transactor rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = PPOB_BASE_KEY, isApproval = false, isTransactor = true)
        result shouldBe "We have rejected your agent’s change to your principal place of business for VAT"
      }

      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = PPOB_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "You have successfully changed your principal place of business for VAT"
      }

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

      "it is for a transactor rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = STAGGER_BASE_KEY, isApproval = false, isTransactor = true)
        result shouldBe "We have rejected your agent’s change to your business VAT Return dates"
      }

      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = STAGGER_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "You have successfully changed your VAT Return dates"
      }

      "it is for a client rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = STAGGER_BASE_KEY, isApproval = false, isTransactor = false)
        result shouldBe "We have rejected the change to your business VAT Return dates"
      }
    }

    "return the expected subject for a deregister secure message" when {
      "it is for a transactor approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval = true, isTransactor = true)
        result shouldBe "We have accepted your agent’s request to cancel your VAT registration"
      }

      "it is for a transactor rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval = false, isTransactor = true)
        result shouldBe "We have rejected your agent’s request to cancel your business’ VAT registration"
      }

      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "We have accepted your request to cancel your VAT registration"
      }

      "it is for a client rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval = false, isTransactor = false)
        result shouldBe "We have rejected your request to cancel your VAT registration"
      }
    }

    "return the expected subject for a bank details secure message" when {
      "it is for a transactor approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval = true, isTransactor = true)
        result shouldBe "Your agent has successfully changed your bank details for VAT repayments"
      }

      "it is for a transactor rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval = false, isTransactor = true)
        result shouldBe "We have rejected your agent’s change to your bank details for VAT repayments"
      }

      "it is for a client approved change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "You have successfully changed your bank details for VAT repayments"
      }

      "it is for a client rejected change" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval = false, isTransactor = false)
        result shouldBe "We have rejected the change to your bank details for VAT repayments"
      }
    }

    "return the expected subject for an opt out secure message" when {
      "it is for a non-represented user" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = OPT_OUT_BASE_KEY, isApproval = true, isTransactor = false)
        result shouldBe "You have opted out of Making Tax Digital for VAT"
      }

      "it is for a represented user" in {
        val result = service.getSubjectForBaseKey(baseSubjectKey = OPT_OUT_BASE_KEY, isApproval = true, isTransactor = true)
        result shouldBe "Your agent has opted your business out of Making Tax Digital for VAT"
      }
    }

    "return the expected subject for a website change secure message" when {
        "it is for a transactor approved changed" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = WEBSITE_BASE_KEY, isApproval = true, isTransactor = true)
          result shouldBe "Your agent has successfully changed your website address for VAT"
        }

        "it is for a client approved change" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = WEBSITE_BASE_KEY, isApproval = true, isTransactor = false)
          result shouldBe "You have successfully changed your website address for VAT"
        }

        "it is for a transactor rejected change" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = WEBSITE_BASE_KEY, isApproval = false, isTransactor = true)
          result shouldBe "We have rejected your agent’s change of website address for VAT"
        }

        "it is for a client rejected change" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = WEBSITE_BASE_KEY, isApproval = false, isTransactor = false)
          result shouldBe "We have rejected your request to change your website address for VAT"
        }

        "it is for a transactor approved removal" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = WEBSITE_BASE_KEY, isApproval = true, isTransactor = true, isRemoval = true)
          result shouldBe "Your agent has successfully removed your website address for VAT"
        }

        "it is for a client approved removal" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = WEBSITE_BASE_KEY, isApproval = true, isTransactor = false, isRemoval = true)
          result shouldBe "You have successfully removed your website address for VAT"
        }

        "it is for a transactor rejected removal" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = WEBSITE_BASE_KEY, isApproval = false, isTransactor = true, isRemoval = true)
          result shouldBe "We have rejected your agent’s request to remove your website address for VAT"
        }

        "it is for a client rejected removal" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = WEBSITE_BASE_KEY, isApproval = false, isTransactor = false, isRemoval = true)
          result shouldBe "We have rejected your request to remove your website address for VAT"
        }
    }

    "return the expected subject for a contact numbers secure message" when {
        "it is for a transactor approved change" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = CONTACT_NUMBERS_BASE_KEY, isApproval = true, isTransactor = true)
          result shouldBe "Your agent has successfully changed your contact details for VAT"
        }

        "it is for a client approved change" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = CONTACT_NUMBERS_BASE_KEY, isApproval = true, isTransactor = false)
          result shouldBe "You have successfully changed your contact details for VAT"
        }

        "it is for a transactor rejected change" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = CONTACT_NUMBERS_BASE_KEY, isApproval = false, isTransactor = true)
          result shouldBe "We have rejected your agent’s request to change your contact details for VAT"
        }

        "it is for a client rejected change" in {
          val result = service.getSubjectForBaseKey(baseSubjectKey = CONTACT_NUMBERS_BASE_KEY, isApproval = false, isTransactor = false)
          result shouldBe "We have rejected your request to change your contact details for VAT"
        }
    }
  }

  private def setupSuccessResponse = {
    (mockConnector.sendMessage(_: SecureCommsServiceRequestModel)(_: ExecutionContext))
      .expects(*, *)
      .returns(Right(true))
  }
}
