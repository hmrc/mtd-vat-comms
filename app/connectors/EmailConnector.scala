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
import models.emailRendererModels.EmailRequestModel
import models.responseModels.EmailRendererResponseModel
import models.{EmailBadRequest, ErrorModel, NotFoundNoMatch}
import play.api.http.Status._
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.LoggerUtil.logWarnEitherError

import scala.concurrent.{ExecutionContext, Future}

class EmailConnector @Inject()(httpClient: HttpClient, appConfig: AppConfig) {

  type EmailResponse = Either[ErrorModel, EmailRendererResponseModel]

  implicit object SendEmailRequestHttpReads extends HttpReads[EmailResponse] {

    override def read(method: String, url: String, response: HttpResponse): EmailResponse =
      response.status match {
        case ACCEPTED => Right(EmailRendererResponseModel(ACCEPTED))
        case BAD_REQUEST => Left(EmailBadRequest)
        case NOT_FOUND => Left(NotFoundNoMatch)
        case _ => Left(ErrorModel(response.status.toString, response.body))
      }
  }

  def sendEmailRequest(input: EmailRequestModel)
                      (implicit ec: ExecutionContext): Future[EmailResponse] = {

    implicit val hc: HeaderCarrier = HeaderCarrier()

    httpClient.POST[EmailRequestModel, EmailResponse](appConfig.emailServiceUrl, input).map { response =>
      logWarnEitherError(response)
    }
  }
}
