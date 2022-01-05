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

package config

import com.google.inject.ImplementedBy
import config.{ConfigKeys => Keys}
import javax.inject.{Inject, Singleton}
import play.api._
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@ImplementedBy(classOf[MicroserviceAppConfig])
trait AppConfig {
  val configuration: Configuration

  val retryIntervalMillis: Long

  def sendSecureCommsMessageUrl(service: String, regNumber: String, communicationId: String): String

  val queuePollingWaitTime: Int
  val initialWaitTime: Int
  val queueItemExpirySeconds: Int
  val pollingToggle: Boolean

  val emailServiceUrl: String

  val secureCommsServiceUrl: String
  val desAuthorisationToken: String
  val desEnvironment: String

  val tribunalUrl: String

  val manageVatSubscriptionUrl: String

  val vatSummaryUrl: String

  def mtdSignUpUrl(vrn: String): String
}

@Singleton
class MicroserviceAppConfig @Inject()(val runModeConfiguration: Configuration, environment: Environment,
                                      implicit val sc: ServicesConfig)
  extends AppConfig {

  override val configuration: Configuration = runModeConfiguration
  protected def mode: Mode = environment.mode

  override lazy val retryIntervalMillis: Long = runModeConfiguration.getMillis(Keys.failureRetryAfterProperty)

  private lazy val desBase: String = sc.baseUrl(Keys.desBase)
  override def sendSecureCommsMessageUrl(service: String, regNumber: String, communicationId: String): String =
  s"$desBase/secure-comms-alert/service/$service/registration-number/$regNumber/communications/$communicationId"
  override val desAuthorisationToken: String = sc.getString(Keys.desAuthorisationToken)
  override val desEnvironment: String = sc.getString(Keys.desEnvironment)

  override lazy val queuePollingWaitTime: Int = sc.getInt(Keys.queuePollingInterval)

  override lazy val initialWaitTime: Int = sc.getInt(Keys.queueInitialWait)

  override lazy val queueItemExpirySeconds: Int = sc.getInt(Keys.queueItemExpiry)

  override lazy val pollingToggle: Boolean = sc.getBoolean(Keys.queueToggleProperty)

  private lazy val emailServiceBase: String = sc.baseUrl(Keys.emailServiceBase)
  override lazy val emailServiceUrl: String = s"$emailServiceBase/hmrc/email"

  private lazy val secureCommsServiceBase: String = sc.baseUrl(Keys.secureCommsServiceBase)
  override lazy val secureCommsServiceUrl: String = s"$secureCommsServiceBase/messages"

  override lazy val tribunalUrl: String = sc.getString(Keys.tribunalUrl)

  private lazy val manageVatSubscriptionHost: String = sc.getString(Keys.manageVatSubscriptionHost)
  override lazy val manageVatSubscriptionUrl: String =
    manageVatSubscriptionHost + sc.getString(Keys.manageVatSubscriptionUri)

  private lazy val vatSummaryHost: String = sc.getString(Keys.vatSummaryHost)
  override lazy val vatSummaryUrl: String =
    vatSummaryHost + sc.getString(Keys.vatSummaryUri)

  override def mtdSignUpUrl(vrn: String): String = sc.getString(Keys.vatSignUpHost) + sc.getString(Keys.reSignUpUri) + vrn
}
