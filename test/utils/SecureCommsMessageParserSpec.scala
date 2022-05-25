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

package utils

import base.BaseSpec
import common.Constants.ChannelPreferences.PAPER
import common.Constants.EmailStatus.VERIFIED
import common.Constants.FormatPreferences.TEXT
import common.Constants.LanguagePreferences.ENGLISH
import common.Constants.NotificationPreference.EMAIL
import models.secureMessageAlertModels._
import models.secureMessageAlertModels.messageTypes._
import models.{SecureCommsMessageModel, SpecificParsingError}
import play.api.libs.json.{JsObject, JsValue, Json}
import utils.SecureCommsMessageTestData.{ResponseAsModel, Responses}

class SecureCommsMessageParserSpec extends BaseSpec {

  val stringToParse: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|123456789</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|testBusinessName</P>" +
    "<P>EFFECTIVE DATE OF DEREGISTRATION|20181227</P><P>TRANSACTOR EMAIL|test@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL|testCustomer@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL STATUS|VERIFIED</P><P>NOTIFICATION PREFERENCE|EMAIL</P><P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P>" +
    "<P>FORMAT PREFERENCE|TEXT</P>]]>"

  val expectedJson: JsObject = Json.obj(
    "templateId" -> "VRT41A_SM1A",
    "vrn" -> "123456789",
    "formBundleReference" -> "092000003080",
    "businessName" -> "testBusinessName",
    "effectiveDateOfDeregistration" -> "20181227",
    "transactorEmail" -> "test@email.co.uk",
    "customerEmail" -> "testCustomer@email.co.uk",
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
    ("Email Change", Responses.expectedResponseEmailChange, ResponseAsModel.expectedResponseEmailChange),
    ("Web Address Change", Responses.expectedResponseWebAddress, ResponseAsModel.expectedResponseWebAddress)
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
        bankDetails <- Seq(Some(BankDetailsModel("businessName", "8493483729273", "32-12-22")), None)
        staggerDetails <- Seq(Some(StaggerDetailsModel("EE02", "NewStartDate", "NewEndDate", "OldStagger", "OldStartDate" ,"OldEndDate")), None)
        oEmail <- Seq(Some("anOriginalEmail@aproperhost.co.uk"), None)
        mandationStatus <- Seq(Some("3"), None)
        websiteAddress <- Seq(Some("https://www.web-address.co.uk"), None)
        contactNumbers <- Seq(Some(ContactNumbersModel("01225654321", "YES", "07571234567", "NO")), None)
      } yield {
        SecureCommsMessageModel("", "", "", "", effectiveDODR, addressDetails, bankDetails, staggerDetails, oEmail,
          mandationStatus, websiteAddress, contactNumbers, TransactorModel("", ""), CustomerModel("", ""), PreferencesModel("", "", "", ""))
      }).filter { passedForwardModel =>
        Seq(
          passedForwardModel.effectiveDateOfDeregistration,
          passedForwardModel.addressDetails,
          passedForwardModel.bankAccountDetails,
          passedForwardModel.staggerDetails,
          passedForwardModel.originalEmailAddress,
          passedForwardModel.mandationStatus,
          passedForwardModel.websiteAddress,
          passedForwardModel.contactNumbers).count(_.nonEmpty) > 1
      }

      allInvalidCombinations.foreach { model =>
        s"the following combination of optional parameters are used: ${model.effectiveDateOfDeregistration}," +
          s"${model.addressDetails}, ${model.bankAccountDetails}, ${model.staggerDetails}, " +
          s"${model.originalEmailAddress}, ${model.mandationStatus}, ${model.websiteAddress}, ${model.contactNumbers}" in {
          SecureCommsMessageParser.parseModel(model) shouldBe Left(SpecificParsingError)
        }
      }
    }
  }
}
