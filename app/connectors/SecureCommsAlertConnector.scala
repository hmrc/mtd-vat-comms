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
import com.google.inject.Inject
import models._
import models.responseModels.{SecureCommsErrorResponseModel, SecureCommsResponseModel}
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.Authorization
import utils.LoggerUtil._

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsAlertConnector @Inject()(wsClient: WSClient,
                                          appConfig: AppConfig) {

  def getSecureCommsMessage(service: String, regNumber: String, communicationId: String)
                           (implicit ec: ExecutionContext): Future[Either[ErrorModel, SecureCommsResponseModel]] = {
    lazy val headers: Seq[(String, String)] = HeaderCarrier(
      Some(Authorization(s"Bearer ${appConfig.desAuthorisationToken}"))
    ).withExtraHeaders("Environment" -> appConfig.desEnvironment).headers

    val url = appConfig.sendSecureCommsMessageUrl(service, regNumber, communicationId)
    wsClient.url(url).withHeaders(headers: _*).get().map { response =>
      logWarnEitherError(handleResponse(response))
    }
  }

  def handleResponse(response: WSResponse): Either[ErrorModel, SecureCommsResponseModel] = {
    response.status match {
      case OK => handleOk(response)
      case BAD_REQUEST => handleBadRequest(response)
      case NOT_FOUND => handleNotFound(response)
      case status: Int =>
          logWarn("[SecureCommsAlertConnector][getSecureCommsMessage] - " +
            s"Unexpected error encountered. Status: '$status', Body: '${response.body}'")
          Left(ErrorModel(s"${response.status}", response.body))
    }
  }

  private def handleOk(wSResponse: WSResponse): Either[ErrorModel, SecureCommsResponseModel] = {
    Json.parse(wSResponse.body).validate[SecureCommsResponseModel].asOpt match {
      case Some(responseModel) => Right(responseModel)
      case None =>
        logWarn("[SecureCommsAlertConnector][getSecureCommsMessage] - " +
          "Failed to validate response to SecureCommsResponseModel")
        logDebug(s"[SecureCommsAlertConnector][getSecureCommsMessage] - Body: '${wSResponse.body}'")
        Left(UnableToParseSecureCommsResponseError)
    }
  }

  private def handleBadRequest(wSResponse: WSResponse): Left[ErrorModel, SecureCommsResponseModel] = {
    Json.parse(wSResponse.body).validate[SecureCommsErrorResponseModel].asOpt match {
      case Some(error) => Left(ErrorModel(error.code, error.reason))
      case None =>
        logWarn("[SecureCommsAlertConnector][getSecureCommsMessage] - " +
          s"Failed to validate error response to SecureCommsErrorResponseModel. Body: '${wSResponse.body}'")
        Left(UnableToParseSecureCommsErrorResponseError)
    }
  }

  private def handleNotFound(wSResponse: WSResponse): Left[ErrorModel, SecureCommsResponseModel] = {
    Json.parse(wSResponse.body).validate[SecureCommsErrorResponseModel].asOpt match {
      case Some(_) => Left(NotFoundNoMatch)
      case None =>
        logWarn("[SecureCommsAlertConnector][getSecureCommsMessage] - " +
          s"Failed to validate error response to SecureCommsErrorResponseModel. Body: '${wSResponse.body}'")
        Left(UnableToParseSecureCommsErrorResponseError)
    }
  }
}
