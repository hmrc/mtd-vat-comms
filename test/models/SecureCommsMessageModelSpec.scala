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

package models

import base.BaseSpec
import play.api.libs.json.Json
import utils.SecureCommsMessageTestData.JsonModels._
import utils.SecureCommsMessageTestData.JsonModelForModels._
import utils.SecureCommsMessageTestData.Responses._

class SecureCommsMessageModelSpec extends BaseSpec {
  "SecureCommsMessageModel" when {

    "parsing from JSON" should {

      "correctly parse every field" in {
        validJsonEverything.as[SecureCommsMessageModel] shouldBe expectedResponseEverything
      }

      "correctly parse when optional fields are missing - DeRegistration" in {
        validJsonDeRegistration.as[SecureCommsMessageModel] shouldBe expectedResponseDeRegistration
      }

      "correctly parse when optional fields are missing - PPOB Change" in {
        validJsonPPOBChange.as[SecureCommsMessageModel] shouldBe expectedResponsePPOBChange
      }

      "correctly parse when optional fields are missing - Repayment Bank Account Change" in {
        validJsonRepaymentBankAccountChange.as[SecureCommsMessageModel] shouldBe expectedResponseBankRepaymentAccountChange
      }

      "correctly parse when optional fields are missing - Email Change" in {
        validJsonEmailChange.as[SecureCommsMessageModel] shouldBe expectedResponseEmailChange
      }

      "correctly parse when optional fields are missing - Stagger Change" in {
        validJsonVatStaggerChange.as[SecureCommsMessageModel] shouldBe expectedResponseStagger
      }

      "correctly parse when optional fields are missing - Business Name Change" in {
        validJsonBusinessNameChange.as[SecureCommsMessageModel] shouldBe expectedResponseBusinessNameChange
      }
    }

    "serializing to JSON" should {

      "correctly parse every field" in {
        Json.toJson(expectedResponseEverything) shouldBe validJsonForModelEverything
      }

      "correctly parse when optional fields are missing - DeRegistration" in {
        Json.toJson(expectedResponseDeRegistration) shouldBe validJsonForModelDeRegistration
      }

      "correctly parse when optional fields are missing - PPOB Change" in {
        Json.toJson(expectedResponsePPOBChange) shouldBe validJsonForModelPPOBChange
      }

      "correctly parse when optional fields are missing - Repayment Bank Account Change" in {
        Json.toJson(expectedResponseBankRepaymentAccountChange) shouldBe validJsonForModelRepaymentsBankAccountChangeModel
      }

      "correctly parse when optional fields are missing - Email Change" in {
        Json.toJson(expectedResponseEmailChange) shouldBe validJsonForModelEmailAddressChange
      }

      "correctly parse when optional fields are missing - Stagger Change" in {
        Json.toJson(expectedResponseStagger) shouldBe validJsonForModelVATStaggerChange
      }

      "correctly parse when optional fields are missing - Business Name Change" in {
        Json.toJson(expectedResponseBusinessNameChange) shouldBe validJsonForModelBusinessNameChange
      }
    }
  }
}
