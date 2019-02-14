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
import models.ErrorModel
import models.responseModels.SecureCommsServiceResponseModel
import models.secureCommsServiceModels.SecureCommsServiceRequestModel
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsServiceConnector@Inject()(wSClient: WSClient, appConfig: AppConfig) {
  def sendMessage(request: SecureCommsServiceRequestModel)
                 (implicit ec: ExecutionContext): Future[Either[ErrorModel, SecureCommsServiceResponseModel]] = {

    wSClient.url(appConfig.secureCommsServiceUrl)
        .post(Json.toJson(request)).map { response: WSResponse =>
      response.status match {
        case CREATED => Json.parse(response.body).validate[SecureCommsServiceResponseModel].asOpt match {
          case Some(value) => Right(value)
          case None => Left(ErrorModel("ERROR_PARSING_RESPONSE", "There was an error parsing the response from the Secure Comms Service"))
        }
        case otherStatus => Left(ErrorModel(s"${otherStatus}_RECEIVED_FROM_SERVICE", response.body))
      }
    }

  }
}
