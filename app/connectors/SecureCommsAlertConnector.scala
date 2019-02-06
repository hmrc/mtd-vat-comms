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
import models.responseModels.{SecureCommsErrorResponseModel, SecureCommsResponseModel}
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsAlertConnector @Inject()(wsClient: WSClient,
                                          appConfig: AppConfig) {

  //noinspection ScalaStyle
  def getSecureCommsMessage(service: String, regNumber: String, communicationId: String)
                           (implicit ec: ExecutionContext): Future[Either[ErrorModel, SecureCommsResponseModel]] = {
    val url = appConfig.secureCommsUrl(service, regNumber, communicationId)
    wsClient.url(url).get().map { response =>
      response.status match {
        case OK => Json.parse(response.body).validate[SecureCommsResponseModel].asOpt match {
          case Some(responseModel) => Right(responseModel)
          case None => Left(UnableToParseSecureCommsResponseError)
        }
        case BAD_REQUEST => Json.parse(response.body).validate[SecureCommsErrorResponseModel].asOpt match {
          case Some(error) => Left(ErrorModel(error.code, error.reason))
          case None => Left(UnableToParseSecureCommsErrorResponseError)
        }
        case _: Int => Left(ErrorModel(s"${response.status}", response.body))
      }
    }
  }

}
