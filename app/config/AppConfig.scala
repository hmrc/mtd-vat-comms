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

package config

import com.google.inject.ImplementedBy
import javax.inject.{Inject, Singleton}
import play.api._
import uk.gov.hmrc.play.config.ServicesConfig
import config.{ConfigKeys => Keys}

@ImplementedBy(classOf[MicroserviceAppConfig])
trait AppConfig extends ServicesConfig {
  val retryIntervalMillis: Long

  def sendSecureCommsMessageUrl(service: String, regNumber: String, communicationId: String): String

  val queuePollingWaitTime: Int
  val initialWaitTime: Int
  val pollingToggle: Boolean

  val emailServiceUrl: String

  val secureCommsServiceUrl: String
  val desAuthorisationToken: String
  val desEnvironment: String

  val tribunalUrl: String

  val manageVatSubscriptionUrl: String
}

@Singleton
class MicroserviceAppConfig @Inject()(val runModeConfiguration: Configuration, environment: Environment)
  extends AppConfig {

  override def mode: Mode.Mode = environment.mode

  override lazy val retryIntervalMillis: Long = runModeConfiguration.getMilliseconds(Keys.failureRetryAfterProperty)
    .getOrElse(throw new RuntimeException(s"retry interval not specified"))

  private lazy val desBase: String = baseUrl(Keys.desBase)
  override def sendSecureCommsMessageUrl(service: String, regNumber: String, communicationId: String): String =
  s"$desBase/secure-comms-alert/service/$service/registration-number/$regNumber/communications/$communicationId"
  override val desAuthorisationToken: String = runModeConfiguration.getString(Keys.desAuthorisationToken).getOrElse("")
  override val desEnvironment: String = runModeConfiguration.getString(Keys.desEnvironment).getOrElse("ist0")

  override lazy val queuePollingWaitTime: Int = getInt(Keys.queuePollingInterval)

  override lazy val initialWaitTime: Int = getInt(Keys.queueInitialWait)

  override lazy val pollingToggle: Boolean = getBoolean(Keys.queueToggleProperty)

  private lazy val emailServiceBase: String = baseUrl(Keys.emailServiceBase)
  override lazy val emailServiceUrl: String = s"$emailServiceBase/hmrc/email"

  private lazy val secureCommsServiceBase: String = baseUrl(Keys.secureCommsServiceBase)
  override lazy val secureCommsServiceUrl: String = s"$secureCommsServiceBase/messages"

  override lazy val tribunalUrl: String = getString(Keys.tribunalUrl)

  private lazy val manageVatSubscriptionBase: String = baseUrl(Keys.manageVatSubscriptionBase)
  override lazy val manageVatSubscriptionUrl: String =
  manageVatSubscriptionBase + getString(Keys.manageVatSubscriptionUri)
}
