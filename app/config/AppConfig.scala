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

  val desProtocol: String
  val desHost: String
  val desPort: String
  def sendSecureCommsMessageUrl(service: String, regNumber: String, communicationId: String): String

  val queuePollingWaitTime: Int
  val initialWaitTime: Int
  val pollingToggle: Boolean
  val failureRetryAfterProperty: String

  val emailServiceProtocol: String
  val emailServiceHost: String
  val emailServicePort: String
  val emailServiceUrl: String

  val secureCommsServiceProtocol: String
  val secureCommsServiceHost: String
  val secureCommsServicePort: String
  val secureCommsServiceUrl: String

  val tribunalUrl: String

  val manageVatSubscriptionFrontendProtocol: String
  val manageVatSubscriptionFrontendHost: String
  val manageVatSubscriptionFrontendPort: String
  val manageVatSubscriptionFrontendUri: String
  def changeBusinessDetailsUrl: String
}

@Singleton
class MicroserviceAppConfig @Inject()(val runModeConfiguration: Configuration, environment: Environment)
  extends AppConfig {

  override def mode: Mode.Mode = environment.mode
  lazy val failureRetryAfterProperty: String = Keys.failureRetryAfterProperty
  lazy val queuePollingWaitTimeProperty: String = Keys.queuePollingInterval
  lazy val initialWaitProperty: String = Keys.queueInitialWait
  lazy val queueToggleProperty: String = Keys.queueToggleProperty

  lazy val defaultPollingWaitTime: Int = 30

  lazy val retryIntervalMillis: Long = runModeConfiguration.getMilliseconds(failureRetryAfterProperty)
    .getOrElse(throw new RuntimeException(s"$failureRetryAfterProperty not specified"))

  lazy val desProtocol: String = runModeConfiguration.getString(Keys.secureMessageAlertProtocol).getOrElse("http")
  lazy val desHost: String = runModeConfiguration.getString(Keys.secureMessageAlertHost).getOrElse("localhost")
  lazy val desPort: String = runModeConfiguration.getString(Keys.secureMessageAlertPort).getOrElse("9175")
  def sendSecureCommsMessageUrl(service: String, regNumber: String, communicationId: String): String =
    s"$desProtocol://$desHost:$desPort/secure-comms-alert/" +
      s"service/$service/registration-number/$regNumber/communications/$communicationId"

  lazy val queuePollingWaitTime: Int = runModeConfiguration.getInt(queuePollingWaitTimeProperty)
    .getOrElse(defaultPollingWaitTime)

  lazy val initialWaitTime: Int = runModeConfiguration.getInt(initialWaitProperty).getOrElse(30)

  lazy val pollingToggle: Boolean = runModeConfiguration.getBoolean(queueToggleProperty).getOrElse(true)

  lazy val emailServiceProtocol: String = runModeConfiguration.getString(Keys.emailServiceProtocol).getOrElse("http")
  lazy val emailServiceHost: String = runModeConfiguration.getString(Keys.emailServiceHost).getOrElse("localhost")
  lazy val emailServicePort: String = runModeConfiguration.getString(Keys.emailServicePort).getOrElse("8300")
  lazy val emailServiceUrl: String = s"$emailServiceProtocol://$emailServiceHost:$emailServicePort/hmrc/email"

  override val secureCommsServiceProtocol: String = runModeConfiguration.getString(Keys.secureCommsServiceProtocol).getOrElse("http")
  override val secureCommsServiceHost: String = runModeConfiguration.getString(Keys.secureCommsServiceHost).getOrElse("localhost")
  override val secureCommsServicePort: String = runModeConfiguration.getString(Keys.secureCommsServicePort).getOrElse("9175")
  override val secureCommsServiceUrl: String = s"$secureCommsServiceProtocol://$secureCommsServiceHost:$secureCommsServicePort/messages"

  lazy val tribunalUrl: String = runModeConfiguration.getString(Keys.tribunalUrl).getOrElse(
    "https://www.gov.uk/tax-tribunal/appeal-to-tribunal")

  lazy val manageVatSubscriptionFrontendProtocol: String = runModeConfiguration
    .getString(Keys.manageVatSubscriptionProtocol).getOrElse("http")

  lazy val manageVatSubscriptionFrontendHost: String = runModeConfiguration
    .getString(Keys.manageVatSubscriptionHost).getOrElse("localhost")
  lazy val manageVatSubscriptionFrontendPort: String = runModeConfiguration
    .getString(Keys.manageVatSubscriptionPort).getOrElse("9150")
  lazy val manageVatSubscriptionFrontendUri: String = runModeConfiguration
    .getString(Keys.manageVatSubscriptionUri).getOrElse("/vat-through-software/account/change-business-details")

  def changeBusinessDetailsUrl: String = s"$manageVatSubscriptionFrontendProtocol://" +
    s"$manageVatSubscriptionFrontendHost:$manageVatSubscriptionFrontendPort$manageVatSubscriptionFrontendUri"
}
