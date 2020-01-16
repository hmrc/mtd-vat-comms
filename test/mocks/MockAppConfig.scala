/*
 * Copyright 2020 HM Revenue & Customs
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

package mocks

import config.AppConfig
import play.api.Mode.Mode
import play.api.{Configuration, Mode}

class MockAppConfig(val runModeConfiguration: Configuration,
                    val mode: Mode = Mode.Test,
                    override val pollingToggle: Boolean = false) extends AppConfig {

  override val configuration: Configuration = runModeConfiguration

  def sendSecureCommsMessageUrl(service: String, regNumber: String, communicationId: String): String =
    s"/secure-comms-alert/service/$service/registration-number/$regNumber/communications/$communicationId"
  override val desAuthorisationToken: String = "EYAUE_AISOD92834894"
  override val desEnvironment: String = "ist0"

  override val retryIntervalMillis: Long = 10000L
  override val queuePollingWaitTime: Int = 1
  override val initialWaitTime: Int = 0

  override val emailServiceUrl: String = s"/hmrc/email"

  override val secureCommsServiceUrl: String = s"/messages"

  override val tribunalUrl: String = "https://www.gov.uk/tax-tribunal/appeal-to-tribunal"

  override val vatSummaryUrl: String = "/vat-through-software/vat-overview"

  override val manageVatSubscriptionUrl: String = "/vat-through-software/account/change-business-details"

  override def mtdSignUpUrl(vrn: String): String = s"/sign-up/$vrn"

  override val queueItemExpirySeconds: Int = 30
}
