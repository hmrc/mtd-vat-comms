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
    val EFFECTIVE_DOD = "effectiveDateOfDeRegistration"
    val TRANSACTOR_EMAIL = "transactorEmail"
    val TRANSACTOR_NAME = "transactorName"
    val AL1 = "addressLine1"
    val AL2 = "addressLine2"
    val AL3 = "addressLine3"
    val AL4 = "addressLine4"
    val AL5 = "addressLine5"
    val POST_CODE = "postCode"
    val COUNTRY_NAME = "countryName"
    val ACCOUNT_NUMBER = "bankAccountNumber"
    val SORT_CODE = "bankSortCode"
    val STAGGER = "stagger"
    val O_EMAIL_ADDRESS = "originalEmailAddress"
    val C_EMAIL = "customerEmail"
    val C_EMAIL_STATUS = "customerEmailStatus"
    val N_PREFS = "notificationPreferences"
    val C_PREFS = "channelPreferences"
    val L_PREFS = "languagePreferences"
    val F_PREFS = "formatPreferences"
  }
}
