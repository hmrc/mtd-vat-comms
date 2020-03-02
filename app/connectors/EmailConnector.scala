/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors

import config.AppConfig
import javax.inject.Inject
import models.{BadRequest, ErrorModel, NotFoundNoMatch}
import models.emailRendererModels.EmailRequestModel
import models.responseModels.EmailRendererResponseModel
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
        case BAD_REQUEST => Left(BadRequest)
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
