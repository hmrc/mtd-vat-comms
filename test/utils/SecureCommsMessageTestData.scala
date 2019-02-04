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

import models.SecureCommsMessageModel
import models.secureCommsModels._
import play.api.libs.json.{JsObject, Json}
import utils.Constants.ChannelPreferences.PAPER
import utils.Constants.EmailStatus.VERIFIED
import utils.Constants.FormatPreferences.TEXT
import utils.Constants.LanguagePreferences.ENGLISH
import utils.Constants.NotificationPreference.EMAIL
import utils.Constants.SecureCommsMessageFields._

object SecureCommsMessageTestData {
  object JsonModels {
    val validJsonEverything: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      EFFECTIVE_DOD -> "20181227",
      TRANSACTOR_EMAIL -> "lydia@carryburdens.tam",
      TRANSACTOR_NAME -> "Pack Mule",
      AL1 -> "4 Cloud District",
      AL2 -> "Whiterun",
      AL3 -> "",
      AL4 -> "",
      AL5 -> "",
      POST_CODE -> "TA11RI",
      COUNTRY_NAME -> "Skyrim",
      ACCOUNT_NUMBER -> "12039831",
      SORT_CODE -> "11-11-11",
      STAGGER -> "12jje7uw",
      O_EMAIL_ADDRESS -> "sofia@whiterunstables.co.tam",
      C_EMAIL -> "dovahkin@riften.tam",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> PAPER,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonDeRegistration: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      EFFECTIVE_DOD -> "20181227",
      TRANSACTOR_EMAIL -> "lydia@carryburdens.tam",
      TRANSACTOR_NAME -> "Pack Mule",
      C_EMAIL -> "dovahkin@riften.tam",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> PAPER,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonPPOBChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      TRANSACTOR_EMAIL -> "lydia@carryburdens.tam",
      TRANSACTOR_NAME -> "Pack Mule",
      AL1 -> "4 Cloud District",
      AL2 -> "Whiterun",
      AL3 -> "",
      AL4 -> "",
      AL5 -> "",
      POST_CODE -> "TA11RI",
      COUNTRY_NAME -> "Skyrim",
      C_EMAIL -> "dovahkin@riften.tam",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> PAPER,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonRepaymentBankAccountChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      TRANSACTOR_EMAIL -> "lydia@carryburdens.tam",
      TRANSACTOR_NAME -> "Pack Mule",
      ACCOUNT_NUMBER -> "12039831",
      SORT_CODE -> "11-11-11",
      C_EMAIL -> "dovahkin@riften.tam",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> PAPER,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonVatStaggerChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      TRANSACTOR_EMAIL -> "lydia@carryburdens.tam",
      TRANSACTOR_NAME -> "Pack Mule",
      STAGGER -> "12jje7uw",
      C_EMAIL -> "dovahkin@riften.tam",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> PAPER,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonEmailChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      TRANSACTOR_EMAIL -> "lydia@carryburdens.tam",
      TRANSACTOR_NAME -> "Pack Mule",
      O_EMAIL_ADDRESS -> "dragonborn@winterhold.tam",
      C_EMAIL -> "dovahkin@riften.tam",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> PAPER,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonBusinessNameChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "Companions Guild",
      TRANSACTOR_EMAIL -> "lydia@carryburdens.tam",
      TRANSACTOR_NAME -> "Pack Mule",
      C_EMAIL -> "dovahkin@riften.tam",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> PAPER,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )
  }

  object Responses {
    val expectedResponseEverything = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      Some("20181227"),
      Some(AddressDetailsModel("4 Cloud District", "Whiterun", "", "", "", "TA11RI", "Skyrim")),
      Some(BankDetailsModel("12039831", "11-11-11")),
      Some("12jje7uw"),
      Some("sofia@whiterunstables.co.tam"),
      TransactorModel("lydia@carryburdens.tam", "Pack Mule"),
      CustomerModel("dovahkin@riften.tam", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseDeRegistration = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      Some("20181227"),
      None,
      None,
      None,
      None,
      TransactorModel("lydia@carryburdens.tam", "Pack Mule"),
      CustomerModel("dovahkin@riften.tam", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponsePPOBChange = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      Some(AddressDetailsModel("4 Cloud District", "Whiterun", "", "", "", "TA11RI", "Skyrim")),
      None,
      None,
      None,
      TransactorModel("lydia@carryburdens.tam", "Pack Mule"),
      CustomerModel("dovahkin@riften.tam", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseBankRepaymentAccountChange = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      Some(BankDetailsModel("12039831", "11-11-11")),
      None,
      None,
      TransactorModel("lydia@carryburdens.tam", "Pack Mule"),
      CustomerModel("dovahkin@riften.tam", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseStagger = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      Some("12jje7uw"),
      None,
      TransactorModel("lydia@carryburdens.tam", "Pack Mule"),
      CustomerModel("dovahkin@riften.tam", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseEmailChange = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      None,
      Some("dragonborn@winterhold.tam"),
      TransactorModel("lydia@carryburdens.tam", "Pack Mule"),
      CustomerModel("dovahkin@riften.tam", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseBusinessNameChange = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "Companions Guild",
      None,
      None,
      None,
      None,
      None,
      TransactorModel("lydia@carryburdens.tam", "Pack Mule"),
      CustomerModel("dovahkin@riften.tam", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )
  }
}
