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

package utils

import base.BaseSpec
import models.secureMessageAlertModels._
import models.secureMessageAlertModels.messageTypes._
import models.{SecureCommsMessageModel, SpecificParsingError}
import play.api.libs.json.{JsObject, JsValue, Json}
import common.Constants.ChannelPreferences.PAPER
import common.Constants.EmailStatus.VERIFIED
import common.Constants.FormatPreferences.TEXT
import common.Constants.LanguagePreferences.ENGLISH
import common.Constants.NotificationPreference.EMAIL
import utils.SecureCommsMessageTestData.{ResponseAsModel, Responses}

class SecureCommsMessageParserSpec extends BaseSpec {

  val stringToParse: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|100065579</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|CoC Company Holdings Ltd</P>" +
    "<P>EFFECTIVE DATE OF DEREGISTRATION|20181227</P><P>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</P>" +
    "<P>CUSTOMER EMAIL|info@CoCHoldings.co.uk</P>" +
    "<P>CUSTOMER EMAIL STATUS|VERIFIED</P><P>NOTIFICATION PREFERENCE|EMAIL</P><P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P>" +
    "<P>FORMAT PREFERENCE|TEXT</P>]]>"

  val expectedJson: JsObject = Json.obj(
    "templateId" -> "VRT41A_SM1A",
    "vrn" -> "100065579",
    "formBundleReference" -> "092000003080",
    "businessName" -> "CoC Company Holdings Ltd",
    "effectiveDateOfDeregistration" -> "20181227",
    "transactorEmail" -> "Info_in_FB@CoCHoldingsLtd.co.uk",
    "customerEmail" -> "info@CoCHoldings.co.uk",
    "customerEmailStatus" -> VERIFIED,
    "notificationPreference" -> EMAIL,
    "channelPreference" -> PAPER,
    "languagePreference" -> ENGLISH,
    "formatPreference" -> TEXT
  )

  "parseMessage" should {
    "successfully parse an incoming string into valid json" in {
      val parsedJson = SecureCommsMessageParser.parseMessage(stringToParse).right.get.as[JsObject]
      val parsedJsonAsPrettyString = parsedJson.fields.sortBy(_._1).toMap[String, JsValue]
      val expectedJsonAsPrettyString = expectedJson.fields.sortBy(_._1).toMap[String, JsValue]

      parsedJsonAsPrettyString shouldBe expectedJsonAsPrettyString
    }
  }

  val parsingTest: Seq[(String, SecureCommsMessageModel, MessageModel)] = Seq(
    ("DeRegistration", Responses.expectedResponseDeRegistration, ResponseAsModel.expectedResponseDeRegistration),
    ("PPOB Change", Responses.expectedResponsePPOBChange, ResponseAsModel.expectedResponsePPOBChange),
    ("Repayments Bank Account Change",
      Responses.expectedResponseBankRepaymentAccountChange, ResponseAsModel.expectedResponseRepaymentsBankAccountChange),
    ("VAT Stagger", Responses.expectedResponseStagger, ResponseAsModel.expectedResponseStagger),
    ("Email Change", Responses.expectedResponseEmailChange, ResponseAsModel.expectedResponseEmailChange)
  )

  parsingTest.foreach { case (testName, genericResponse, modelResponse) =>
    s"Generic Message Model for $testName" should {
      "parse into the correct model" in {
        SecureCommsMessageParser.parseModel(genericResponse) shouldBe Right(modelResponse)
      }
    }
  }

  "An incorrectly populated generic model" should {
    "throw an error" when {
      val allInvalidCombinations = (for {
        effectiveDODR <- Seq(Some("20180121"), None)
        addressDetails <- Seq(Some(AddressDetailsModel("4 NotReal Way", "A Place", "", "", "", "SW42NR", "Fantasy Land")), None)
        bankDetails <- Seq(Some(BankDetailsModel("Bank of Tamriel", "8493483729273", "32-12-22")), None)
        stagger <- Seq(Some("EE02"), None)
        oEmail <- Seq(Some("anOriginalEmail@aproperhost.co.uk"), None)
      } yield {
        SecureCommsMessageModel("", "", "", "", effectiveDODR, addressDetails, bankDetails, stagger, oEmail,
          TransactorModel("", ""), CustomerModel("", ""), PreferencesModel("", "", "", ""))
      }).filter { passedForwardModel =>
        Seq(
          passedForwardModel.effectiveDateOfDeregistration,
          passedForwardModel.addressDetails,
          passedForwardModel.bankAccountDetails,
          passedForwardModel.stagger,
          passedForwardModel.originalEmailAddress).count(_.nonEmpty) > 1
      }

      allInvalidCombinations.foreach { model =>
        s"the following combination of optional parameters are used: ${model.effectiveDateOfDeregistration}," +
          s"${model.addressDetails}, ${model.bankAccountDetails}, ${model.stagger}, ${model.originalEmailAddress}" in {
          SecureCommsMessageParser.parseModel(model) shouldBe Left(SpecificParsingError)
        }
      }
    }
  }
}
