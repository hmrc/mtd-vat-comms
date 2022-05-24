/*
 * Copyright 2022 HM Revenue & Customs
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
    ("Business Name Change", expectedResponseBusinessNameChange, validJsonForModelBusinessNameChange.as[BusinessNameChangeModel])
  )

  jsonModelMapping.foreach { case (testName, expectedResponse, testArgument) =>
    s"Parsing json for $testName" should {
      "return a valid model" in {
        testArgument shouldBe expectedResponse
      }
    }
  }

}
