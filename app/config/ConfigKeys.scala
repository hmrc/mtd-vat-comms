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

  val secureMessageAlertProtocol: String = "microservice.services.des.protocol"
  val secureMessageAlertHost: String = "microservice.services.des.host"
  val secureMessageAlertPort: String = "microservice.services.des.port"

  val queuePollingInterval: String = "queue.pollingInterval"
  val queueInitialWait: String = "queue.initialWait"
  val queueToggleProperty: String = "queue.toggle"


  val emailServiceProtocol: String = "microservice.services.emailService.protocol"
  val emailServiceHost: String = "microservice.services.emailService.host"
  val emailServicePort: String = "microservice.services.emailService.port"

  val secureCommsServiceProtocol: String = "microservice.services.secureCommsService.protocol"
  val secureCommsServiceHost: String = "microservice.services.secureCommsService.host"
  val secureCommsServicePort: String = "microservice.services.secureCommsService.port"
}
