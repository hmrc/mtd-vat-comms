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
import models.secureMessageAlertModels._
import models.secureMessageAlertModels.messageTypes._
import play.api.libs.json.{JsObject, Json}
import common.Constants.ChannelPreferences.PAPER
import common.Constants.EmailStatus.VERIFIED
import common.Constants.FormatPreferences.TEXT
import common.Constants.LanguagePreferences.ENGLISH
import common.Constants.NotificationPreference.EMAIL
import common.Constants.SecureCommsMessageFields._

object SecureCommsMessageTestData {

  val newStaggerStartExample = "20190502"
  val newStaggerEndExample = "20190423"
  val previousStaggerExample = "previousStaggerCode"
  val previousStaggerStartExample = "20180912"
  val previousStaggerEndExample = "20180411"

  val transactorModel: TransactorModel = TransactorModel("Info_in_FB@CoCHoldingsLtd.co.uk", "Pack Mule")

  object JsonModels {
    val validJsonEverything: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      EFFECTIVE_DOD -> "20181227",
      TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
      TRANSACTOR_NAME -> "Pack Mule",
      AL1 -> "4 Cloud District",
      AL2 -> "Whiterun",
      AL3 -> "",
      AL4 -> "",
      AL5 -> "",
      POST_CODE -> "TA11RI",
      COUNTRY_NAME -> "Skyrim",
      ACCOUNT_NAME -> "Bank of Tamriel",
      ACCOUNT_NUMBER -> "12039831",
      SORT_CODE -> "11-11-11",
      STAGGER -> "12jje7uw",
      NEW_STAGGER_START_DATE -> newStaggerStartExample,
      NEW_STAGGER_END_DATE -> newStaggerEndExample,
      PREVIOUS_STAGGER -> previousStaggerExample,
      PREVIOUS_STAGGER_START_DATE -> previousStaggerStartExample,
      PREVIOUS_STAGGER_END_DATE -> previousStaggerEndExample,
      O_EMAIL_ADDRESS -> "sofia@whiterunstables.co.tam",
      MANDATION_STATUS -> "3",
      WEBSITE_ADDRESS -> "https://www.web-address.co.uk",
      PRIMARY_PHONENUMBER -> "01225123456",
      PRIMARY_PHONENUMBER_CHANGED -> "YES",
      MOBILE_NUMBER -> "07578123456",
      MOBILE_NUMBER_CHANGED -> "NO",
      C_EMAIL -> "info@CoCHoldings.co.uk",
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
      TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
      TRANSACTOR_NAME -> "Pack Mule",
      C_EMAIL -> "info@CoCHoldings.co.uk",
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
      TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
      TRANSACTOR_NAME -> "Pack Mule",
      AL1 -> "4 Cloud District",
      AL2 -> "Whiterun",
      AL3 -> "",
      AL4 -> "",
      AL5 -> "",
      POST_CODE -> "TA11RI",
      COUNTRY_NAME -> "Skyrim",
      C_EMAIL -> "info@CoCHoldings.co.uk",
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
      TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
      TRANSACTOR_NAME -> "Pack Mule",
      ACCOUNT_NAME -> "Bank of Tamriel",
      ACCOUNT_NUMBER -> "12039831",
      SORT_CODE -> "11-11-11",
      C_EMAIL -> "info@CoCHoldings.co.uk",
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
      TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
      TRANSACTOR_NAME -> "Pack Mule",
      STAGGER -> "12jje7uw",
      NEW_STAGGER_START_DATE -> newStaggerStartExample,
      NEW_STAGGER_END_DATE -> newStaggerEndExample,
      PREVIOUS_STAGGER -> previousStaggerExample,
      PREVIOUS_STAGGER_START_DATE -> previousStaggerStartExample,
      PREVIOUS_STAGGER_END_DATE -> previousStaggerEndExample,
      C_EMAIL -> "info@CoCHoldings.co.uk",
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
      TRANSACTOR_EMAIL -> "",
      TRANSACTOR_NAME -> "",
      O_EMAIL_ADDRESS -> "dragonborn@winterhold.tam",
      C_EMAIL -> "info@CoCHoldings.co.uk",
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
      TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
      TRANSACTOR_NAME -> "Pack Mule",
      C_EMAIL -> "info@CoCHoldings.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> PAPER,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonContactNumbersChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT12A_SM12A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "Some Company Limited",
      TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
      TRANSACTOR_NAME -> "Pack Mule",
      C_EMAIL -> "info@CoCHoldings.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      PRIMARY_PHONENUMBER -> "01225123456",
      PRIMARY_PHONENUMBER_CHANGED -> "YES",
      MOBILE_NUMBER -> "07578123456",
      MOBILE_NUMBER_CHANGED -> "NO",
      N_PREFS -> EMAIL,
      C_PREFS -> PAPER,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonWebAddressChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT12A_SM14A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "Some Company Limited",
      WEBSITE_ADDRESS -> "https://www.web-address.co.uk",
      TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
      TRANSACTOR_NAME -> "Pack Mule",
      C_EMAIL -> "info@CoCHoldings.co.uk",
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
      Some(BankDetailsModel("Bank of Tamriel", "12039831", "11-11-11")),
      Some(StaggerDetailsModel("12jje7uw", newStaggerStartExample, newStaggerEndExample,
        previousStaggerExample, previousStaggerStartExample, previousStaggerEndExample)),
      Some("sofia@whiterunstables.co.tam"),
      Some("3"),
      Some("https://www.web-address.co.uk"),
      Some(ContactNumbersModel("01225123456", "YES", "07578123456", "NO")),
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
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
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
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
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseBankRepaymentAccountChange = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      Some(BankDetailsModel("Bank of Tamriel", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
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
      Some(StaggerDetailsModel("12jje7uw", newStaggerStartExample, newStaggerEndExample,
        previousStaggerExample, previousStaggerStartExample, previousStaggerEndExample)),
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
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
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseOptOut = SecureCommsMessageModel(
      "CC07C_SM11C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      None,
      None,
      Some("3"),
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseWebAddress = SecureCommsMessageModel(
      "VRT12A_SM14A",
      "100065579",
      "092000003080",
      "Some Company Limited",
      None,
      None,
      None,
      None,
      None,
      None,
      Some("https://www.web-address.co.uk"),
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseContactNumbers = SecureCommsMessageModel(
      "VRT12A_SM12A",
      "100065579",
      "092000003080",
      "Some Company Limited",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      Some(ContactNumbersModel("01225123456", "YES", "07578123456", "NO")),
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
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
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseNoTransactor = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("dovahkin@riften.tam", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )
  }

  object SendSecureMessageModels {

    val messageModelDeRegistrationInvalidTemplate = SecureCommsMessageModel(
      "NO SUCH ID",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val deRegistrationValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT23C_SM7C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val deRegistrationValidRejectedTransactorRequest = SecureCommsMessageModel(
      "VRT15C_SM8C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val deRegistrationValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT23A_SM7A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val deRegistrationValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT15A_SM8A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val bankDetailsValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT12C_SM3C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      Some(BankDetailsModel("Bank of Tamriel", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val bankDetailsValidRejectedTransactorRequest = SecureCommsMessageModel(
      "VRT14C_SM4C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      Some(BankDetailsModel("Bank of Tamriel", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val bankDetailsValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM3A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      Some(BankDetailsModel("Bank of Tamriel", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val bankDetailsValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM4A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      Some(BankDetailsModel("Bank of Tamriel", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val staggerValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT12C_SM5C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      Some(StaggerDetailsModel("MM", newStaggerStartExample, newStaggerEndExample,
        previousStaggerExample, previousStaggerStartExample, previousStaggerEndExample)),
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val staggerLeaveAnnualAccountingValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT12C_SM5C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      Some(StaggerDetailsModel("MM", newStaggerStartExample, newStaggerEndExample,
        "YA", previousStaggerStartExample, previousStaggerEndExample)),
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val staggerinvalidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT12C_SM5C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      Some(StaggerDetailsModel("InvalidStaggerCode", "", "", "", "", "")),
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val staggerValidRejectedTransactorRequest = SecureCommsMessageModel(
      "VRT14C_SM6C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      Some(StaggerDetailsModel("MC", "", "", "", "", "")),
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val staggerLeaveAnnualAccountingValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM5A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      Some(StaggerDetailsModel("MB", newStaggerStartExample, newStaggerEndExample,
        "YE", previousStaggerStartExample, previousStaggerEndExample)),
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val staggerValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM5A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      Some(StaggerDetailsModel("MB", newStaggerStartExample, newStaggerEndExample,
        previousStaggerExample, previousStaggerStartExample, previousStaggerEndExample)),
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val staggerValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM6A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      Some(StaggerDetailsModel("12jje7uw", "", "", "", "", "")),
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val ppobValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT12C_SM1C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      Some(AddressDetailsModel("4 Cloud District", "Whiterun", "", "", "", "TA11RI", "Skyrim")),
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val ppobValidRejectedTransactorRequest = SecureCommsMessageModel(
      "VRT14C_SM2C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      Some(AddressDetailsModel("4 Cloud District", "Whiterun", "", "", "", "TA11RI", "Skyrim")),
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val ppobValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      Some(AddressDetailsModel("4 Cloud District", "Whiterun", "", "", "", "TA11RI", "Skyrim")),
      None,
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val ppobValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM2A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      Some(AddressDetailsModel("4 Cloud District", "Whiterun", "", "", "", "TA11RI", "Skyrim")),
      None,
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val emailValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM9A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      None,
      Some("dragonborn@winterhold.tam"),
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val optOutRequest = SecureCommsMessageModel(
      "CC07A_SM11A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      None,
      None,
      Some("3"),
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val optOutRequestRepresented = SecureCommsMessageModel(
      "CC07C_SM11C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      None,
      None,
      Some("3"),
      None,
      None,
      TransactorModel("agent@CoCHoldings.co.uk", "CoC Agent"),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val emailValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM10A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      None,
      Some("dragonborn@winterhold.tam"),
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val websiteValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM15A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      None,
      None,
      None,
      Some("http://www.web-address.co.uk"),
      None,
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val websiteValidApprovedClientRequest: SecureCommsMessageModel = websiteValidRejectedClientRequest.copy(
      templateId = "VRT12A_SM14A")

    val websiteValidApprovedTransactorRequest: SecureCommsMessageModel = websiteValidRejectedClientRequest.copy(
      templateId = "VRT12C_SM14C", transactorDetails = transactorModel)

    val websiteValidRejectedTransactorRequest: SecureCommsMessageModel = websiteValidRejectedClientRequest.copy(
      templateId = "VRT14C_SM15C", transactorDetails = transactorModel, websiteAddress = Some(""))

    val contactNumbersValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM13A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      Some(ContactNumbersModel("01225123456", "YES", "07578123456", "NO")),
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val contactNumbersValidApprovedClientRequest: SecureCommsMessageModel = contactNumbersValidRejectedClientRequest.copy(
      templateId = "VRT12A_SM12A")

    val contactNumbersValidApprovedTransactorRequest: SecureCommsMessageModel = contactNumbersValidRejectedClientRequest.copy(
      templateId = "VRT12C_SM12C", transactorDetails = transactorModel)

    val contactNumbersValidRejectedTransactorRequest: SecureCommsMessageModel = contactNumbersValidRejectedClientRequest.copy(
      templateId = "VRT14C_SM13C", transactorDetails = transactorModel, contactNumbers = Some(ContactNumbersModel("01225654321", "YES", "07578654321", "NO")))
  }

  object JsonModelForModels {
    val validJsonForModelEverything: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      EFFECTIVE_DOD -> "20181227",
      ADDRESS_DETAILS -> Json.obj(
        AL1 -> "4 Cloud District",
        AL2 -> "Whiterun",
        AL3 -> "",
        AL4 -> "",
        AL5 -> "",
        POST_CODE -> "TA11RI",
        COUNTRY_NAME -> "Skyrim"
      ),
      BANK_DETAILS -> Json.obj(
        ACCOUNT_NAME -> "Bank of Tamriel",
        ACCOUNT_NUMBER -> "12039831",
        SORT_CODE -> "11-11-11"
      ),
      STAGGER_DETAILS -> Json.obj(
        STAGGER -> "12jje7uw",
        NEW_STAGGER_START_DATE -> newStaggerStartExample,
        NEW_STAGGER_END_DATE -> newStaggerEndExample,
        PREVIOUS_STAGGER -> previousStaggerExample,
        PREVIOUS_STAGGER_START_DATE -> previousStaggerStartExample,
        PREVIOUS_STAGGER_END_DATE -> previousStaggerEndExample
      ),
      O_EMAIL_ADDRESS -> "sofia@whiterunstables.co.tam",
      MANDATION_STATUS -> "3",
      WEBSITE_ADDRESS -> "https://www.web-address.co.uk",
      CONTACT_NUMBERS -> Json.obj(
        PRIMARY_PHONENUMBER -> "01225123456",
        PRIMARY_PHONENUMBER_CHANGED -> "YES",
        MOBILE_NUMBER -> "07578123456",
        MOBILE_NUMBER_CHANGED -> "NO"
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
        TRANSACTOR_NAME -> "Pack Mule"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelDeRegistration: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      EFFECTIVE_DOD -> "20181227",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
        TRANSACTOR_NAME -> "Pack Mule"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelPPOBChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      ADDRESS_DETAILS -> Json.obj(
        AL1 -> "4 Cloud District",
        AL2 -> "Whiterun",
        AL3 -> "",
        AL4 -> "",
        AL5 -> "",
        POST_CODE -> "TA11RI",
        COUNTRY_NAME -> "Skyrim"
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
        TRANSACTOR_NAME -> "Pack Mule"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelRepaymentsBankAccountChangeModel: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      BANK_DETAILS -> Json.obj(
        ACCOUNT_NAME -> "Bank of Tamriel",
        ACCOUNT_NUMBER -> "12039831",
        SORT_CODE -> "11-11-11"
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
        TRANSACTOR_NAME -> "Pack Mule"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelVATStaggerChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      STAGGER_DETAILS -> Json.obj(
        STAGGER -> "12jje7uw",
        NEW_STAGGER_START_DATE -> newStaggerStartExample,
        NEW_STAGGER_END_DATE -> newStaggerEndExample,
        PREVIOUS_STAGGER -> previousStaggerExample,
        PREVIOUS_STAGGER_START_DATE -> previousStaggerStartExample,
        PREVIOUS_STAGGER_END_DATE -> previousStaggerEndExample
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
        TRANSACTOR_NAME -> "Pack Mule"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelEmailAddressChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      O_EMAIL_ADDRESS -> "dragonborn@winterhold.tam",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "",
        TRANSACTOR_NAME -> ""
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelBusinessNameChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "Companions Guild",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
        TRANSACTOR_NAME -> "Pack Mule"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelWebAddressChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT12A_SM14A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "Some Company Limited",
      WEBSITE_ADDRESS -> "https://www.web-address.co.uk",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
        TRANSACTOR_NAME -> "Pack Mule"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelContactNumbersChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT12A_SM12A",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "Some Company Limited",
      CONTACT_NUMBERS -> Json.obj(
        PRIMARY_PHONENUMBER -> "01225123456",
        PRIMARY_PHONENUMBER_CHANGED -> "YES",
        MOBILE_NUMBER -> "07578123456",
        MOBILE_NUMBER_CHANGED -> "NO"
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
        TRANSACTOR_NAME -> "Pack Mule"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelOptOut: JsObject = Json.obj(
      TEMPLATE_ID -> "CC07C_SM11C",
      VRN -> "100065579",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "CoC Company Holdings Ltd",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "Info_in_FB@CoCHoldingsLtd.co.uk",
        TRANSACTOR_NAME -> "Pack Mule"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "info@CoCHoldings.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      MANDATION_STATUS -> "3",
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> PAPER,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )
  }

  object ResponseAsModel {
    val expectedResponseDeRegistration = DeRegistrationModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT),
      "20181227"
    )

    val expectedResponsePPOBChange = PPOBChangeModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT),
      AddressDetailsModel("4 Cloud District", "Whiterun", "", "", "", "TA11RI", "Skyrim")
    )

    val expectedResponseRepaymentsBankAccountChange = RepaymentsBankAccountChangeModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT),
      BankDetailsModel("Bank of Tamriel", "12039831", "11-11-11")
    )

    val expectedResponseStagger = VATStaggerChangeModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT),
      StaggerDetailsModel("12jje7uw", newStaggerStartExample, newStaggerEndExample,
        previousStaggerExample, previousStaggerStartExample, previousStaggerEndExample)
    )

    val expectedResponseEmailChange = EmailAddressChangeModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      TransactorModel("", ""),
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT),
      "dragonborn@winterhold.tam"
    )

    val expectedResponseBusinessNameChange = BusinessNameChangeModel(
      "VRT41A_SM1A",
      "100065579",
      "092000003080",
      "Companions Guild",
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseOptOut = OptOutModel(
      "CC07C_SM11C",
      "100065579",
      "092000003080",
      "CoC Company Holdings Ltd",
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      "3",
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseWebAddress = WebAddressChangeModel(
      "VRT12A_SM14A",
      "100065579",
      "092000003080",
      "Some Company Limited",
      transactorModel,
      CustomerModel("info@CoCHoldings.co.uk", VERIFIED),
      "https://www.web-address.co.uk",
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )
  }

}
