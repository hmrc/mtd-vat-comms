/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors

import config.AppConfig
import javax.inject.Inject
import models._
import models.secureCommsServiceModels.SecureCommsServiceRequestModel
import play.api.http.Status._
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.LoggerUtil.{logWarnEitherError, logWarn}

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsServiceConnector @Inject()(httpClient: HttpClient, appConfig: AppConfig) {

  type SecureCommsResponse = Either[ErrorModel, Boolean]

  implicit object SendMessageReads extends HttpReads[SecureCommsResponse] {

    override def read(method: String, url: String, response: HttpResponse): SecureCommsResponse = {
      response.status match {
        case CREATED => Right(true)
        case BAD_REQUEST =>
          logWarn(s"[SendMessageReads][read] - Bad request received from Secure Comms service: ${response.body}")
          Left(BadRequest)
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
