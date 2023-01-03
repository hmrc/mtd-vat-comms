/*
 * Copyright 2023 HM Revenue & Customs
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

import com.google.inject.Inject
import config.AppConfig
import models._
import models.responseModels.SecureCommsResponseModel
import play.api.http.Status._
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpReads, HttpResponse}
import utils.LoggerUtil

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsAlertConnector @Inject()(httpClient: HttpClient,
                                          appConfig: AppConfig) extends LoggerUtil {

  type SecureCommsAlertResponse = Either[ErrorModel, SecureCommsResponseModel]

  implicit object GetSecureCommsMessageReads extends HttpReads[SecureCommsAlertResponse] {

    override def read(method: String, url: String, response: HttpResponse): SecureCommsAlertResponse =
      response.status match {
        case OK => handleOk(response)
        case BAD_REQUEST =>
          logger.warn(s"[GetSecureCommsMessageReads][read] - Bad request received from DES. Body: '${response.body}'")
          Left(BadRequest)
        case NOT_FOUND =>
          logger.warn(s"[GetSecureCommsMessageReads][read] - Not found error received from DES. Body: '${response.body}'")
          Left(NotFoundNoMatch)
        case status =>
          logger.warn("[SecureCommsAlertConnector][getSecureCommsMessage] - Unexpected error received from DES. " +
            s"Status code: '$status', Body: '${response.body}'")
          Left(ErrorModel(s"${response.status}", response.body))
      }
  }

  def getSecureCommsMessage(service: String, regNumber: String, communicationId: String)
                           (implicit ec: ExecutionContext): Future[SecureCommsAlertResponse] = {

    val desHeaders = Seq("Authorization" -> s"Bearer ${appConfig.desAuthorisationToken}", "Environment" -> appConfig.desEnvironment)

    implicit val hc: HeaderCarrier = HeaderCarrier(authorization = None)

    val url = appConfig.sendSecureCommsMessageUrl(service, regNumber, communicationId)

    httpClient.GET[SecureCommsAlertResponse](url,headers = desHeaders)
  }

  private def handleOk(response: HttpResponse): SecureCommsAlertResponse =
    Json.parse(response.body).validate[SecureCommsResponseModel].asOpt match {
      case Some(responseModel) =>
        logger.debug(s"[GetSecureCommsMessageReads][read] - Successfully parsed SecureCommsResponseModel: $responseModel")
        Right(responseModel)
      case None =>
        logger.warn("[GetSecureCommsMessageReads][read] - Failed to validate response to SecureCommsResponseModel")
        logger.debug(s"[GetSecureCommsMessageReads][read] - Body: '${response.body}'")
        Left(GenericParsingError)
    }
}
