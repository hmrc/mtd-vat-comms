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

object ConfigKeys {

  val failureRetryAfterProperty: String = "queue.retryAfter"

  val secureCommsProtocol: String = "microservice.services.secureComms.protocol"
  val secureCommsHost: String = "microservice.services.secureComms.host"
  val secureCommsPort: String = "microservice.services.secureComms.port"
  val queuePollingInterval: String = "queue.pollingInterval"
  val queueInitialWait: String = "queue.initialWait"
  val queueToggleProperty: String = "queue.toggle"


  val emailRendererProtocol: String = "microservice.services.emailRenderer.protocol"
  val emailRendererHost: String = "microservice.services.emailRenderer.host"
  val emailRendererPort: String = "microservice.services.emailRenderer.port"
}
