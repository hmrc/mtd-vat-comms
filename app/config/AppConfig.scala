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
import com.typesafe.config.Config
import javax.inject.{Inject, Singleton}
import play.api._
import uk.gov.hmrc.play.config.ServicesConfig

@ImplementedBy(classOf[MicroserviceAppConfig])
trait AppConfig {
}

class AwsConfiguration(config: Config) {
  private def optionalString(name: String): Option[String] = if(config.hasPath(name)) Some(config.getString(name)) else None
  private def optionalInt(name: String): Option[Int] = if(config.hasPath(name)) Some(config.getInt(name)) else None
  private def base64Decode(text: String): String = new String(java.util.Base64.getDecoder.decode(text))
}

@Singleton
class MicroserviceAppConfig @Inject()(override val runModeConfiguration: Configuration, environment: Environment) extends AppConfig with ServicesConfig {
  override def mode: Mode.Mode = environment.mode
}
