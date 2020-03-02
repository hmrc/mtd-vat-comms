/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import base.BaseSpec
import models.secureMessageAlertModels.messageTypes._
import utils.SecureCommsMessageTestData.JsonModelForModels._
import utils.SecureCommsMessageTestData.ResponseAsModel._

class MessageTypesSpec extends BaseSpec {

  val jsonModelMapping: Seq[(String, MessageModel, MessageModel)] = Seq(
    ("DeRegistration", expectedResponseDeRegistration, validJsonForModelDeRegistration.as[DeRegistrationModel]),
    ("PPOB Change", expectedResponsePPOBChange, validJsonForModelPPOBChange.as[PPOBChangeModel]),
    ("Repayments Bank Account Change",
      expectedResponseRepaymentsBankAccountChange, validJsonForModelRepaymentsBankAccountChangeModel.as[RepaymentsBankAccountChangeModel]),
    ("VAT Stagger", expectedResponseStagger, validJsonForModelVATStaggerChange.as[VATStaggerChangeModel]),
    ("Email Change", expectedResponseEmailChange, validJsonForModelEmailAddressChange.as[EmailAddressChangeModel]),
    ("Business Name Change", expectedResponseBusinessNameChange, validJsonForModelBusinessNameChange.as[BusinessNameChangeModel]),
    ("Opt Out", expectedResponseOptOut, validJsonForModelOptOut.as[OptOutModel])
  )

  jsonModelMapping.foreach { case (testName, expectedResponse, testArgument) =>
    s"Parsing json for $testName" should {
      "return a valid model" in {
        testArgument shouldBe expectedResponse
      }
    }
  }

}
