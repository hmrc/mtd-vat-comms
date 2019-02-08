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

package services

import connectors.SecureCommsAlertConnector
import com.google.inject.Inject
import models.secureCommsModels.messageTypes.MessageModel
import models.{ErrorModel, GenericParsingError, SecureCommsMessageModel}
import utils.SecureCommsMessageParser

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsAlertService @Inject()(secureCommsAlertConnector: SecureCommsAlertConnector) {

  def getSecureCommsMessage(service: String, regNumber: String, communicationId: String)
                           (implicit ec: ExecutionContext): Future[Either[ErrorModel, MessageModel]] = {
    secureCommsAlertConnector.getSecureCommsMessage(service, regNumber, communicationId).map {
      case Right(response) => SecureCommsMessageParser.parseMessage(response.secureCommText) match {
        case Right(parsedJson) => parsedJson.validate[SecureCommsMessageModel].asOpt match {
          case Some(parsedModel) => SecureCommsMessageParser.parseModel(parsedModel)
          case None => Left(GenericParsingError)
        }
        case Left(error) => Left(error)
      }
      case Left(error) => Left(error)
    }
  }

}
