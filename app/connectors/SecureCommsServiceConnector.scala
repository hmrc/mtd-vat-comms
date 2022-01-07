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

package connectors

import config.AppConfig

import javax.inject.Inject
import models._
import models.secureCommsServiceModels.SecureCommsServiceRequestModel
import play.api.http.Status._
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse}
import uk.gov.hmrc.http.HttpClient
import utils.LoggerUtil

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsServiceConnector @Inject()(httpClient: HttpClient, appConfig: AppConfig) extends LoggerUtil {

  type SecureCommsResponse = Either[ErrorModel, Boolean]

  implicit object SendMessageReads extends HttpReads[SecureCommsResponse] {

    override def read(method: String, url: String, response: HttpResponse): SecureCommsResponse = {
      response.status match {
        case CREATED => Right(true)
        case BAD_REQUEST =>
          logger.warn(s"[SendMessageReads][read] - Bad request error received from Secure Comms service: ${response.body}")
          Left(BadRequest)
        case CONFLICT =>
          logger.warn(s"[SendMessageReads][read] - Conflict error received from Secure Comms service: ${response.body}")
          Left(ConflictDuplicateMessage)
        case status =>
          logger.warn(s"[SendMessageReads][read] - Unexpected error received from Secure Comms service. " +
            s"Status code: '$status', Body: '${response.body}'")
          Left(ErrorModel(s"${status}_RECEIVED_FROM_SERVICE", response.body))
      }
    }
  }

  def sendMessage(request: SecureCommsServiceRequestModel)
                 (implicit ec: ExecutionContext): Future[SecureCommsResponse] = {

    implicit val hc: HeaderCarrier = HeaderCarrier()

    httpClient.POST[SecureCommsServiceRequestModel, SecureCommsResponse](
      appConfig.secureCommsServiceUrl, request
    )
  }
}
