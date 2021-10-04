/*
 * Copyright 2021 HM Revenue & Customs
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

package services

import connectors.SecureCommsAlertConnector

import javax.inject.Inject
import models.{ErrorModel, GenericParsingError, JsonParsingError, SecureCommsMessageModel}
import utils.{LoggerUtil, SecureCommsMessageParser}

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsAlertService @Inject()(secureCommsAlertConnector: SecureCommsAlertConnector) extends LoggerUtil {

  def getSecureCommsMessage(service: String, regNumber: String, communicationId: String)
                           (implicit ec: ExecutionContext): Future[Either[ErrorModel, SecureCommsMessageModel]] =
    secureCommsAlertConnector.getSecureCommsMessage(service, regNumber, communicationId).map {
      case Right(response) => SecureCommsMessageParser.parseMessage(response.secureCommText) match {
        case Right(parsedJson) => parsedJson.validate[SecureCommsMessageModel].asOpt match {
          case Some(parsedModel) => Right(parsedModel)
          case None =>
            logger.warn(s"[SecureCommsAlertService][getSecureCommsMessage] - " +
              s"${GenericParsingError.code} => ${GenericParsingError.body}")
            Left(GenericParsingError)
        }
        case Left(_) => Left(JsonParsingError)
      }
      case Left(error) => Left(error)
    }
}
