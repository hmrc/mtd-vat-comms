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
import models.emailRendererModels.EmailRendererRequestModel
import models.responseModels.EmailRendererResponseModel
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.http.Status._

import scala.concurrent.{ExecutionContext, Future}

class EmailRendererConnector @Inject()(wsClient: WSClient, appConfig: AppConfig) {

  def sendEmailRequest(input: EmailRendererRequestModel)(implicit ec: ExecutionContext): Future[Either[ErrorModel, EmailRendererResponseModel]] = {
    wsClient.url(appConfig.emailRendererUrl)
      .post(Json.toJson(input))
  }.map { response =>
    response.status match {
      case ACCEPTED => Right(EmailRendererResponseModel(ACCEPTED))
      case _ => Left(ErrorModel(response.status.toString, response.body))
    }
  }

}
