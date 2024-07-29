/*
 * Copyright 2024 HM Revenue & Customs
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

package testutils

import common.Constants.ChannelPreferences.DIGITAL
import common.Constants.EmailStatus.VERIFIED
import common.Constants.FormatPreferences.TEXT
import common.Constants.LanguagePreferences.ENGLISH
import common.Constants.NotificationPreference.EMAIL
import models.SecureCommsMessageModel
import models.secureMessageAlertModels.{CustomerModel, PreferencesModel, TransactorModel}

object TestModels {
  val secureCommsModel = SecureCommsMessageModel(
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
}
