/*
 * Copyright 2021 HM Revenue & Customs
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

import common.Constants.ChannelPreferences._
import common.Constants.EmailStatus.VERIFIED
import common.Constants.FormatPreferences.TEXT
import common.Constants.LanguagePreferences.ENGLISH
import common.Constants.NotificationPreference.EMAIL
import common.Constants.SecureCommsMessageFields._
import models.SecureCommsMessageModel
import models.secureMessageAlertModels._
import models.secureMessageAlertModels.messageTypes._
import play.api.libs.json.{JsObject, Json}

object SecureCommsMessageTestData {

  val newStaggerStartExample = "20190502"
  val newStaggerEndExample = "20190423"
  val previousStaggerExample = "previousStaggerCode"
  val previousStaggerStartExample = "20180912"
  val previousStaggerEndExample = "20180411"

  val transactorModel: TransactorModel = TransactorModel("test@email.co.uk", "testTransactorName")

  object JsonModels {
    val validJsonEverything: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      EFFECTIVE_DOD -> "20181227",
      TRANSACTOR_EMAIL -> "test@email.co.uk",
      TRANSACTOR_NAME -> "testTransactorName",
      AL1 -> "12 Test Street",
      AL2 -> "testTown",
      AL3 -> "",
      AL4 -> "",
      AL5 -> "",
      POST_CODE -> "AB12CD",
      COUNTRY_NAME -> "England",
      ACCOUNT_NAME -> "accountName",
      ACCOUNT_NUMBER -> "12039831",
      SORT_CODE -> "11-11-11",
      STAGGER -> "12jje7uw",
      NEW_STAGGER_START_DATE -> newStaggerStartExample,
      NEW_STAGGER_END_DATE -> newStaggerEndExample,
      PREVIOUS_STAGGER -> previousStaggerExample,
      PREVIOUS_STAGGER_START_DATE -> previousStaggerStartExample,
      PREVIOUS_STAGGER_END_DATE -> previousStaggerEndExample,
      O_EMAIL_ADDRESS -> "example@email.com",
      MANDATION_STATUS -> "3",
      WEBSITE_ADDRESS -> "https://www.web-address.co.uk",
      PRIMARY_PHONENUMBER -> "01225123456",
      PRIMARY_PHONENUMBER_CHANGED -> "YES",
      MOBILE_NUMBER -> "07578123456",
      MOBILE_NUMBER_CHANGED -> "NO",
      C_EMAIL -> "testCustomer@email.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> DIGITAL,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonDeRegistration: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      EFFECTIVE_DOD -> "20181227",
      TRANSACTOR_EMAIL -> "test@email.co.uk",
      TRANSACTOR_NAME -> "testTransactorName",
      C_EMAIL -> "testCustomer@email.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> DIGITAL,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonPPOBChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      TRANSACTOR_EMAIL -> "test@email.co.uk",
      TRANSACTOR_NAME -> "testTransactorName",
      AL1 -> "12 Test Street",
      AL2 -> "testTown",
      AL3 -> "",
      AL4 -> "",
      AL5 -> "",
      POST_CODE -> "AB12CD",
      COUNTRY_NAME -> "England",
      C_EMAIL -> "testCustomer@email.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> DIGITAL,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonRepaymentBankAccountChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      TRANSACTOR_EMAIL -> "test@email.co.uk",
      TRANSACTOR_NAME -> "testTransactorName",
      ACCOUNT_NAME -> "accountName",
      ACCOUNT_NUMBER -> "12039831",
      SORT_CODE -> "11-11-11",
      C_EMAIL -> "testCustomer@email.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> DIGITAL,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonVatStaggerChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      TRANSACTOR_EMAIL -> "test@email.co.uk",
      TRANSACTOR_NAME -> "testTransactorName",
      STAGGER -> "12jje7uw",
      NEW_STAGGER_START_DATE -> newStaggerStartExample,
      NEW_STAGGER_END_DATE -> newStaggerEndExample,
      PREVIOUS_STAGGER -> previousStaggerExample,
      PREVIOUS_STAGGER_START_DATE -> previousStaggerStartExample,
      PREVIOUS_STAGGER_END_DATE -> previousStaggerEndExample,
      C_EMAIL -> "testCustomer@email.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> DIGITAL,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonEmailChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      TRANSACTOR_EMAIL -> "",
      TRANSACTOR_NAME -> "",
      O_EMAIL_ADDRESS -> "example@email.com",
      C_EMAIL -> "testCustomer@email.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> DIGITAL,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonBusinessNameChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "businessName",
      TRANSACTOR_EMAIL -> "test@email.co.uk",
      TRANSACTOR_NAME -> "testTransactorName",
      C_EMAIL -> "testCustomer@email.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> DIGITAL,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonContactNumbersChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT12A_SM12A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "businessName",
      TRANSACTOR_EMAIL -> "test@email.co.uk",
      TRANSACTOR_NAME -> "testTransactorName",
      C_EMAIL -> "testCustomer@email.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      PRIMARY_PHONENUMBER -> "01225123456",
      PRIMARY_PHONENUMBER_CHANGED -> "YES",
      MOBILE_NUMBER -> "07578123456",
      MOBILE_NUMBER_CHANGED -> "NO",
      N_PREFS -> EMAIL,
      C_PREFS -> DIGITAL,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )

    val validJsonWebAddressChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT12A_SM14A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "businessName",
      WEBSITE_ADDRESS -> "https://www.web-address.co.uk",
      TRANSACTOR_EMAIL -> "test@email.co.uk",
      TRANSACTOR_NAME -> "testTransactorName",
      C_EMAIL -> "testCustomer@email.co.uk",
      C_EMAIL_STATUS -> VERIFIED,
      N_PREFS -> EMAIL,
      C_PREFS -> DIGITAL,
      L_PREFS -> ENGLISH,
      F_PREFS -> TEXT
    )
  }

  object Responses {

    val expectedResponseEverything = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      Some("20181227"),
      Some(AddressDetailsModel("12 Test Street", "testTown", "", "", "", "AB12CD", "England")),
      Some(BankDetailsModel("accountName", "12039831", "11-11-11")),
      Some(StaggerDetailsModel("12jje7uw", newStaggerStartExample, newStaggerEndExample,
        previousStaggerExample, previousStaggerStartExample, previousStaggerEndExample)),
      Some("example@email.com"),
      Some("3"),
      Some("https://www.web-address.co.uk"),
      Some(ContactNumbersModel("01225123456", "YES", "07578123456", "NO")),
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseDeRegistration = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponsePPOBChange = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      Some(AddressDetailsModel("12 Test Street", "testTown", "", "", "", "AB12CD", "England")),
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseBankRepaymentAccountChange = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      Some(BankDetailsModel("accountName", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseStagger = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
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
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseStaggerPaper = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
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
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
    )

    val expectedResponseEmailChange = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      None,
      Some("example@email.com"),
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseOptOut = SecureCommsMessageModel(
      "CC07C_SM11C",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      None,
      None,
      Some("3"),
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseWebAddress = SecureCommsMessageModel(
      "VRT12A_SM14A",
      "123456789",
      "092000003080",
      "businessName",
      None,
      None,
      None,
      None,
      None,
      None,
      Some("https://www.web-address.co.uk"),
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseContactNumbers = SecureCommsMessageModel(
      "VRT12A_SM12A",
      "123456789",
      "092000003080",
      "businessName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      Some(ContactNumbersModel("01225123456", "YES", "07578123456", "NO")),
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseBusinessNameChange = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "businessName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseNoTransactor = SecureCommsMessageModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
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
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )
  }

  object SendSecureMessageModels {

    val messageModelDeRegistrationInvalidTemplate = SecureCommsMessageModel(
      "NO SUCH ID",
      "123456789",
      "092000003080",
      "testBusinessName",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val messageModelDeRegistrationInvalidTemplateNoFields = SecureCommsMessageModel(
      "NO SUCH ID",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val deRegistrationValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT23C_SM7C",
      "123456789",
      "092000003080",
      "testBusinessName",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val deRegistrationValidRejectedTransactorRequest = SecureCommsMessageModel(
      "VRT15C_SM8C",
      "123456789",
      "092000003080",
      "testBusinessName",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val deRegistrationValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT23A_SM7A",
      "123456789",
      "092000003080",
      "testBusinessName",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val deRegistrationValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT15A_SM8A",
      "123456789",
      "092000003080",
      "testBusinessName",
      Some("20181227"),
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val bankDetailsValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT12C_SM3C",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      Some(BankDetailsModel("businessName", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val bankDetailsValidRejectedTransactorRequest = SecureCommsMessageModel(
      "VRT14C_SM4C",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      Some(BankDetailsModel("businessName", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val bankDetailsValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM3A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      Some(BankDetailsModel("businessName", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val bankDetailsValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM4A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      Some(BankDetailsModel("businessName", "12039831", "11-11-11")),
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val staggerValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT12C_SM5C",
      "123456789",
      "092000003080",
      "testBusinessName",
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
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val staggerLeaveAnnualAccountingValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT12C_SM5C",
      "123456789",
      "092000003080",
      "testBusinessName",
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
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val staggerValidRejectedTransactorRequest = SecureCommsMessageModel(
      "VRT14C_SM6C",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      Some(StaggerDetailsModel("MC", "", "", "", "", "")),
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val staggerLeaveAnnualAccountingValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM5A",
      "123456789",
      "092000003080",
      "testBusinessName",
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
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val staggerValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM5A",
      "123456789",
      "092000003080",
      "testBusinessName",
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
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val staggerValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM6A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      Some(StaggerDetailsModel("12jje7uw", "", "", "", "", "")),
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val staggerInvalidCodeRequest: SecureCommsMessageModel = staggerValidApprovedClientRequest.copy(
      staggerDetails = Some(StaggerDetailsModel("InvalidStaggerCode", "", "", "", "", ""))
    )

    val staggerInvalidDatesRequest: SecureCommsMessageModel = staggerValidApprovedClientRequest.copy(
      staggerDetails = Some(StaggerDetailsModel("MM", "00000000", "00000000", "YE", "00000000", "00000000"))
    )

    val ppobValidApprovedTransactorRequest = SecureCommsMessageModel(
      "VRT12C_SM1C",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      Some(AddressDetailsModel("12 Test Street", "testTown", "", "", "", "AB12CD", "England")),
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val ppobValidRejectedTransactorRequest = SecureCommsMessageModel(
      "VRT14C_SM2C",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      Some(AddressDetailsModel("12 Test Street", "testTown", "", "", "", "AB12CD", "England")),
      None,
      None,
      None,
      None,
      None,
      None,
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val ppobValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      Some(AddressDetailsModel("12 Test Street", "testTown", "", "", "", "AB12CD", "England")),
      None,
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val ppobValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM2A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      Some(AddressDetailsModel("12 Test Street", "testTown", "", "", "", "AB12CD", "England")),
      None,
      None,
      None,
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val emailValidApprovedClientRequest = SecureCommsMessageModel(
      "VRT12A_SM9A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      None,
      Some("dragonborn@winterhold.tam"),
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val optOutRequest = SecureCommsMessageModel(
      "CC07A_SM11A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      None,
      None,
      Some("3"),
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val optOutRequestRepresented = SecureCommsMessageModel(
      "CC07C_SM11C",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      None,
      None,
      Some("3"),
      None,
      None,
      TransactorModel("agent@CoCHoldings.co.uk", "CoC Agent"),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val emailValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM10A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      None,
      Some("dragonborn@winterhold.tam"),
      None,
      None,
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val websiteValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM15A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      None,
      None,
      None,
      Some("http://www.web-address.co.uk"),
      None,
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val websiteValidApprovedClientRequest: SecureCommsMessageModel = websiteValidRejectedClientRequest.copy(
      templateId = "VRT12A_SM14A")

    val websiteValidApprovedTransactorRequest: SecureCommsMessageModel = websiteValidRejectedClientRequest.copy(
      templateId = "VRT12C_SM14C", transactorDetails = transactorModel)

    val websiteValidRejectedTransactorRequest: SecureCommsMessageModel = websiteValidRejectedClientRequest.copy(
      templateId = "VRT14C_SM15C", transactorDetails = transactorModel, websiteAddress = Some(""))

    val contactNumbersValidRejectedClientRequest = SecureCommsMessageModel(
      "VRT14A_SM13A",
      "123456789",
      "092000003080",
      "testBusinessName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      Some(ContactNumbersModel("01225123456", "YES", "07578123456", "NO")),
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
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
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      EFFECTIVE_DOD -> "20181227",
      ADDRESS_DETAILS -> Json.obj(
        AL1 -> "12 Test Street",
        AL2 -> "testTown",
        AL3 -> "",
        AL4 -> "",
        AL5 -> "",
        POST_CODE -> "AB12CD",
        COUNTRY_NAME -> "England"
      ),
      BANK_DETAILS -> Json.obj(
        ACCOUNT_NAME -> "accountName",
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
      O_EMAIL_ADDRESS -> "example@email.com",
      MANDATION_STATUS -> "3",
      WEBSITE_ADDRESS -> "https://www.web-address.co.uk",
      CONTACT_NUMBERS -> Json.obj(
        PRIMARY_PHONENUMBER -> "01225123456",
        PRIMARY_PHONENUMBER_CHANGED -> "YES",
        MOBILE_NUMBER -> "07578123456",
        MOBILE_NUMBER_CHANGED -> "NO"
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "test@email.co.uk",
        TRANSACTOR_NAME -> "testTransactorName"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelDeRegistration: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      EFFECTIVE_DOD -> "20181227",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "test@email.co.uk",
        TRANSACTOR_NAME -> "testTransactorName"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelPPOBChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      ADDRESS_DETAILS -> Json.obj(
        AL1 -> "12 Test Street",
        AL2 -> "testTown",
        AL3 -> "",
        AL4 -> "",
        AL5 -> "",
        POST_CODE -> "AB12CD",
        COUNTRY_NAME -> "England"
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "test@email.co.uk",
        TRANSACTOR_NAME -> "testTransactorName"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelRepaymentsBankAccountChangeModel: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      BANK_DETAILS -> Json.obj(
        ACCOUNT_NAME -> "accountName",
        ACCOUNT_NUMBER -> "12039831",
        SORT_CODE -> "11-11-11"
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "test@email.co.uk",
        TRANSACTOR_NAME -> "testTransactorName"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelVATStaggerChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      STAGGER_DETAILS -> Json.obj(
        STAGGER -> "12jje7uw",
        NEW_STAGGER_START_DATE -> newStaggerStartExample,
        NEW_STAGGER_END_DATE -> newStaggerEndExample,
        PREVIOUS_STAGGER -> previousStaggerExample,
        PREVIOUS_STAGGER_START_DATE -> previousStaggerStartExample,
        PREVIOUS_STAGGER_END_DATE -> previousStaggerEndExample
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "test@email.co.uk",
        TRANSACTOR_NAME -> "testTransactorName"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelEmailAddressChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      O_EMAIL_ADDRESS -> "example@email.com",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "",
        TRANSACTOR_NAME -> ""
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelBusinessNameChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT41A_SM1A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "businessName",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "test@email.co.uk",
        TRANSACTOR_NAME -> "testTransactorName"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelWebAddressChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT12A_SM14A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "businessName",
      WEBSITE_ADDRESS -> "https://www.web-address.co.uk",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "test@email.co.uk",
        TRANSACTOR_NAME -> "testTransactorName"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelContactNumbersChange: JsObject = Json.obj(
      TEMPLATE_ID -> "VRT12A_SM12A",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "businessName",
      CONTACT_NUMBERS -> Json.obj(
        PRIMARY_PHONENUMBER -> "01225123456",
        PRIMARY_PHONENUMBER_CHANGED -> "YES",
        MOBILE_NUMBER -> "07578123456",
        MOBILE_NUMBER_CHANGED -> "NO"
      ),
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "test@email.co.uk",
        TRANSACTOR_NAME -> "testTransactorName"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )

    val validJsonForModelOptOut: JsObject = Json.obj(
      TEMPLATE_ID -> "CC07C_SM11C",
      VRN -> "123456789",
      FORM_BUNDLE_REFERENCE -> "092000003080",
      BUSINESS_NAME -> "testBusinessName",
      TRANSACTOR_DETAILS -> Json.obj(
        TRANSACTOR_EMAIL -> "test@email.co.uk",
        TRANSACTOR_NAME -> "testTransactorName"
      ),
      CUSTOMER_DETAILS -> Json.obj(
        C_EMAIL -> "testCustomer@email.co.uk",
        C_EMAIL_STATUS -> VERIFIED
      ),
      MANDATION_STATUS -> "3",
      PREFS -> Json.obj(
        N_PREFS -> EMAIL,
        C_PREFS -> DIGITAL,
        L_PREFS -> ENGLISH,
        F_PREFS -> TEXT
      )
    )
  }

  object ResponseAsModel {
    val expectedResponseDeRegistration = DeRegistrationModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT),
      "20181227"
    )

    val expectedResponsePPOBChange = PPOBChangeModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT),
      AddressDetailsModel("12 Test Street", "testTown", "", "", "", "AB12CD", "England")
    )

    val expectedResponseRepaymentsBankAccountChange = RepaymentsBankAccountChangeModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT),
      BankDetailsModel("accountName", "12039831", "11-11-11")
    )

    val expectedResponseStagger = VATStaggerChangeModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT),
      StaggerDetailsModel("12jje7uw", newStaggerStartExample, newStaggerEndExample,
        previousStaggerExample, previousStaggerStartExample, previousStaggerEndExample)
    )

    val expectedResponseEmailChange = EmailAddressChangeModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "testBusinessName",
      TransactorModel("", ""),
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT),
      "example@email.com"
    )

    val expectedResponseBusinessNameChange = BusinessNameChangeModel(
      "VRT41A_SM1A",
      "123456789",
      "092000003080",
      "businessName",
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseOptOut = OptOutModel(
      "CC07C_SM11C",
      "123456789",
      "092000003080",
      "testBusinessName",
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      "3",
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )

    val expectedResponseWebAddress = WebAddressChangeModel(
      "VRT12A_SM14A",
      "123456789",
      "092000003080",
      "businessName",
      transactorModel,
      CustomerModel("testCustomer@email.co.uk", VERIFIED),
      "https://www.web-address.co.uk",
      PreferencesModel(EMAIL, DIGITAL, ENGLISH, TEXT)
    )
  }

}
