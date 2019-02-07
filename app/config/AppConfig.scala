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
  val secureCommsProtocol: String
  val secureCommsHost: String
  val secureCommsPort: String
  def secureCommsUrl(service: String, regNumber: String, communicationId: String): String
  val queuePollingWaitTime: Int
  val initialWaitTime: Int
  val pollingToggle: Boolean
  val failureRetryAfterProperty: String
}

@Singleton
class MicroserviceAppConfig @Inject()(val runModeConfiguration: Configuration, environment: Environment)
  extends AppConfig {

  override def mode: Mode.Mode = environment.mode
  val failureRetryAfterProperty: String = Keys.failureRetryAfterProperty
  val queuePollingWaitTimeProperty: String = Keys.queuePollingInterval
  val initialWaitProperty: String = Keys.queueInitialWait
  val queueToggleProperty: String = Keys.queueToggleProperty

  val defaultPollingWaitTime: Int = 30

  lazy val retryIntervalMillis: Long = runModeConfiguration.getMilliseconds(failureRetryAfterProperty)
    .getOrElse(throw new RuntimeException(s"$failureRetryAfterProperty not specified"))

  lazy val secureCommsProtocol: String = runModeConfiguration.getString(Keys.secureCommsProtocol).getOrElse("http")
  lazy val secureCommsHost: String = runModeConfiguration.getString(Keys.secureCommsHost).getOrElse("localhost")
  lazy val secureCommsPort: String = runModeConfiguration.getString(Keys.secureCommsPort).getOrElse("9068")

  def secureCommsUrl(service: String, regNumber: String, communicationId: String): String =
    s"$secureCommsProtocol://$secureCommsHost:$secureCommsPort/secure-comms-alert/" +
      s"service/$service/registration-number/$regNumber/communications/$communicationId"

  lazy val queuePollingWaitTime: Int = runModeConfiguration.getInt(queuePollingWaitTimeProperty)
    .getOrElse(defaultPollingWaitTime)

  lazy val initialWaitTime: Int = runModeConfiguration.getInt(initialWaitProperty).getOrElse(0)

  lazy val pollingToggle: Boolean = runModeConfiguration.getBoolean(queueToggleProperty).getOrElse(true)

}
