/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors

import config.AppConfig
import com.google.inject.Inject
import models._
import models.responseModels.SecureCommsResponseModel
import play.api.http.Status._
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse}
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.LoggerUtil._

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsAlertConnector @Inject()(httpClient: HttpClient,
                                          appConfig: AppConfig) {

  type SecureCommsAlertResponse = Either[ErrorModel, SecureCommsResponseModel]

  implicit object GetSecureCommsMessageReads extends HttpReads[SecureCommsAlertResponse] {

    override def read(method: String, url: String, response: HttpResponse): SecureCommsAlertResponse =
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

  def getSecureCommsMessage(service: String, regNumber: String, communicationId: String)
                           (implicit ec: ExecutionContext): Future[SecureCommsAlertResponse] = {

    implicit val hc: HeaderCarrier = HeaderCarrier(
      authorization = Some(Authorization(s"Bearer ${appConfig.desAuthorisationToken}")),
      extraHeaders = Seq("Environment" -> appConfig.desEnvironment)
    )
    val url = appConfig.sendSecureCommsMessageUrl(service, regNumber, communicationId)

    httpClient.GET[SecureCommsAlertResponse](url).map { response =>
      logWarnEitherError(response)
    }
  }

  private def handleOk(response: HttpResponse): SecureCommsAlertResponse =
    Json.parse(response.body).validate[SecureCommsResponseModel].asOpt match {
      case Some(responseModel) =>
        logDebug("[SecureCommsAlertConnector][getSecureCommsMessage] - " +
          s"Successfully parsed SecureCommsResponseModel: $responseModel")
        Right(responseModel)
      case None =>
        logWarn("[SecureCommsAlertConnector][getSecureCommsMessage] - " +
          "Failed to validate response to SecureCommsResponseModel")
        logDebug(s"[SecureCommsAlertConnector][getSecureCommsMessage] - Body: '${response.body}'")
        Left(GenericParsingError)
    }

  private def handleBadRequest(response: HttpResponse): Left[ErrorModel, SecureCommsResponseModel] = {
    logWarn(s"[SecureCommsAlertConnector][getSecureCommsMessage] - Bad request. Body: '${response.body}'")
    Left(BadRequest)
  }

  private def handleNotFound(response: HttpResponse): Left[ErrorModel, SecureCommsResponseModel] = {
    logWarn("[SecureCommsAlertConnector][getSecureCommsMessage] - " +
      s"The requested data was not found. Body: '${response.body}'")
    Left(NotFoundNoMatch)
  }
}
