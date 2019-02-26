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
import models.{BadRequest, ErrorModel, NotFoundNoMatch}
import models.emailRendererModels.EmailRequestModel
import models.responseModels.EmailRendererResponseModel
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.http.Status._
import utils.LoggerUtil.logWarnEitherError

import scala.concurrent.{ExecutionContext, Future}

class EmailConnector @Inject()(wsClient: WSClient, appConfig: AppConfig) {

  def sendEmailRequest(input: EmailRequestModel)(implicit ec: ExecutionContext): Future[Either[ErrorModel, EmailRendererResponseModel]] = {
    wsClient.url(appConfig.emailServiceUrl)
      .post(Json.toJson(input))
  }.map { response =>
    logWarnEitherError(handleResponse(response))
  }

  def handleResponse(response: WSResponse): Either[ErrorModel, EmailRendererResponseModel] = {
    response.status match {
      case ACCEPTED => Right(EmailRendererResponseModel(ACCEPTED))
      case BAD_REQUEST => Left(BadRequest)
      case NOT_FOUND => Left(NotFoundNoMatch)
      case _ => Left(ErrorModel(response.status.toString, response.body))
    }
  }

}
