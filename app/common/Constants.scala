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

package common

object Constants {

  object EmailStatus {
    val VERIFIED = "VERIFIED"
    val UNVERIFIED = "UNVERIFIED"
  }

  object NotificationPreference {
    val EMAIL = "EMAIL"
    val SMS = "SMS"
  }

  object ChannelPreferences {
    val DIGITAL = "DIGITAL"
    val PAPER = "PAPER"
  }

  object LanguagePreferences {
    val ENGLISH = "ENGLISH"
    val WELSH = "WELSH"
  }

  object FormatPreferences {
    val TEXT = "TEXT"
    val BRAILLE = "BRAILLE"
    val LARGE_PRINT = "LARGE-PRINT"
    val AUDIO = "AUDIO"
  }

  object SecureCommsMessageFields {
    val TEMPLATE_ID = "templateId"
    val VRN = "vrn"
    val FORM_BUNDLE_REFERENCE = "formBundleReference"
    val BUSINESS_NAME = "businessName"
    val EFFECTIVE_DOD = "effectiveDateOfDeregistration"
    val TRANSACTOR_DETAILS = "transactorDetails"
    val TRANSACTOR_EMAIL = "transactorEmail"
    val TRANSACTOR_NAME = "transactorName"
    val ADDRESS_DETAILS = "addressDetails"
    val AL1 = "addressLine1"
    val AL2 = "addressLine2"
    val AL3 = "addressLine3"
    val AL4 = "addressLine4"
    val AL5 = "addressLine5"
    val POST_CODE = "postCode"
    val COUNTRY_NAME = "countryName"
    val BANK_DETAILS = "bankAccountDetails"
    val ACCOUNT_NAME = "bankAccountName"
    val ACCOUNT_NUMBER = "bankAccountNumber"
    val SORT_CODE = "bankSortCode"
    val STAGGER = "stagger"
    val O_EMAIL_ADDRESS = "originalEmailAddress"
    val MANDATION_STATUS = "mandationStatus"
    val CUSTOMER_DETAILS = "customerDetails"
    val C_EMAIL = "customerEmail"
    val C_EMAIL_STATUS = "customerEmailStatus"
    val PREFS = "preferences"
    val N_PREFS = "notificationPreference"
    val C_PREFS = "channelPreference"
    val L_PREFS = "languagePreference"
    val F_PREFS = "formatPreference"
  }

  object MessageKeys {
    val EMAIL_BASE_KEY = "email"
    val DEREG_BASE_KEY = "dereg"
    val PPOB_BASE_KEY = "ppob"
    val BANK_DETAILS_BASE_KEY = "bankDetails"
    val STAGGER_BASE_KEY = "stagger"
    val OPT_OUT_BASE_KEY = "optOut"
    val APPROVED_SUFFIX = "Approved"
    val REJECTED_SUFFIX = "Rejected"
    val TITLE_KEY_CLIENT = ".title"
    val TITLE_KEY_TRANSACTOR = "Transactor.title"
  }

  object TemplateIdReadableNames {
    val CLIENT_NOTIFICATION_SELF_CHANGE = "newMessageAlert_VRT1214A"
    val CLIENT_NOTIFICATION_AGENT_CHANGE = "newMessageAlert_VRT1214C"
    val AGENT_NOTIFICATION_CHANGE_ACCEPTED = "newMessageAlert_VRT12B"
    val AGENT_NOTIFICATION_CHANGE_REJECTED = "newMessageAlert_VRT14B"
    val AGENT_NOTIFICATION_OPT_OUT = "newMessageAlert_CC07C_SM11C"
  }

  val TAX_IDENTIFIER_MTDVAT = "HMRC-MTD-VAT"

  object SecureCommsServiceFieldValues {
    val MTDP = "mtdp"
  }
}
