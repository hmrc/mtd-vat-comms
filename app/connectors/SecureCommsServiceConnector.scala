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

package connectors

import config.AppConfig
import javax.inject.Inject
import models._
import models.secureCommsServiceModels.SecureCommsServiceRequestModel
import play.api.http.Status._
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.LoggerUtil.{logWarn, logWarnEitherError}

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsServiceConnector @Inject()(httpClient: HttpClient, appConfig: AppConfig) {

  type SecureCommsResponse = Either[ErrorModel, Boolean]

  implicit object SendMessageReads extends HttpReads[SecureCommsResponse] {

    override def read(method: String, url: String, response: HttpResponse): SecureCommsResponse = {
      response.status match {
        case CREATED => Right(true)
        case BAD_REQUEST =>
          logWarn(s"[SendMessageReads][read] - Bad request received from Secure Comms service: ${response.body}")
          Left(SecureCommsServiceBadRequest)
        case NOT_FOUND => Left(handle404Possibilities(response.body))
        case CONFLICT => Left(ConflictDuplicateMessage)
        case otherStatus => Left(ErrorModel(s"${otherStatus}_RECEIVED_FROM_SERVICE", response.body))
      }
    }
  }

  def sendMessage(request: SecureCommsServiceRequestModel)
                 (implicit ec: ExecutionContext): Future[SecureCommsResponse] = {

    implicit val hc: HeaderCarrier = HeaderCarrier()

    httpClient.POST[SecureCommsServiceRequestModel, SecureCommsResponse](
      appConfig.secureCommsServiceUrl, request
    ).map { response =>
      logWarnEitherError(response)
    }
  }

  private def handle404Possibilities(body: String): ErrorModel =
    (body.contains("Taxpayer not found"), body.contains("Email not verified")) match {
      case (true, _) => NotFoundMissingTaxpayer
      case (_, true) => NotFoundUnverifiedEmail
      case (_, _) => ErrorModel("NOT_FOUND", s"Unknown error:\n$body")
    }
}
