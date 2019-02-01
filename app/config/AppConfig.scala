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
}

@Singleton
class MicroserviceAppConfig @Inject()(val runModeConfiguration: Configuration, environment: Environment)
  extends AppConfig {

  override def mode: Mode.Mode = environment.mode
  val failureRetryAfterProperty: String = Keys.failureRetryAfterProperty

  lazy val retryIntervalMillis: Long = runModeConfiguration.getMilliseconds(failureRetryAfterProperty)
    .getOrElse(throw new RuntimeException(s"$failureRetryAfterProperty not specified"))

  val secureCommsHost: String = runModeConfiguration.getString("microservice.services.secureComms.host").getOrElse("localhost")
  val secureCommsPort: String = runModeConfiguration.getString("microservice.services.secureComms.port").getOrElse("9068")
  //noinspection ScalaStyle
  def secureCommsUrl(service: String, regNumber: String, communicationId: String): String = s"$secureCommsHost:$secureCommsPort/secure-comms-alert/service/$service/registration-number/$regNumber/communications/$communicationId"
}
