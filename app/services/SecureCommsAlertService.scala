/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import connectors.SecureCommsAlertConnector
import javax.inject.Inject
import models.{ErrorModel, GenericParsingError, JsonParsingError, SecureCommsMessageModel}
import utils.SecureCommsMessageParser
import utils.LoggerUtil.logWarn

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsAlertService @Inject()(secureCommsAlertConnector: SecureCommsAlertConnector) {

  def getSecureCommsMessage(service: String, regNumber: String, communicationId: String)
                           (implicit ec: ExecutionContext): Future[Either[ErrorModel, SecureCommsMessageModel]] =
    secureCommsAlertConnector.getSecureCommsMessage(service, regNumber, communicationId).map {
      case Right(response) => SecureCommsMessageParser.parseMessage(response.secureCommText) match {
        case Right(parsedJson) => parsedJson.validate[SecureCommsMessageModel].asOpt match {
          case Some(parsedModel) => Right(parsedModel)
          case None =>
            logWarn(s"[SecureCommsAlertService][getSecureCommsMessage] - " +
              s"${GenericParsingError.code} => ${GenericParsingError.body}")
            Left(GenericParsingError)
        }
        case Left(_) => Left(JsonParsingError)
      }
      case Left(error) => Left(error)
    }
}
