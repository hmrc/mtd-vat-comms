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

package connectors

import config.AppConfig
import javax.inject.Inject
import models._
import models.responseModels.SecureCommsServiceResponseModel
import models.secureCommsServiceModels.SecureCommsServiceRequestModel
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import utils.LoggerUtil.logWarnEitherError

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsServiceConnector @Inject()(wSClient: WSClient, appConfig: AppConfig) {

  def sendMessage(request: SecureCommsServiceRequestModel)
                 (implicit ec: ExecutionContext): Future[Either[ErrorModel, SecureCommsServiceResponseModel]] = {

    wSClient.url(appConfig.secureCommsServiceUrl)
      .post(Json.toJson(request))
      .map {
        r: WSResponse => logWarnEitherError(handleResponse(r))
      }

  }

  private def handleResponse(response: WSResponse) = {
    response.status match {
      case CREATED => handle201Possibilities(Json.parse(response.body).validate[SecureCommsServiceResponseModel].asOpt)
      case BAD_REQUEST => Left(BadRequestUnknownTaxIdentifier)
      case NOT_FOUND => Left(handle404Possibilities(response.body))
      case CONFLICT => Left(ConflictDuplicateMessage)
      case otherStatus => Left(ErrorModel(s"${otherStatus}_RECEIVED_FROM_SERVICE", response.body))
    }
  }

  private def handle201Possibilities(input: Option[SecureCommsServiceResponseModel]) = {
    input match {
      case Some(value) => Right(value)
      case None => Left(UnableToParseSecureCommsServiceResponse)
    }
  }

  private def handle404Possibilities(body: String): ErrorModel = {
    (body.contains("Taxpayer not found"), body.contains("Email not verified")) match {
      case (true, _) => NotFoundMissingTaxpayer
      case (_, true) => NotFoundUnverifiedEmail
      case (_, _) => ErrorModel("NOT_FOUND", s"Unknown error:\n$body")
    }
  }
}
