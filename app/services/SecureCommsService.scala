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

import connectors.SecureCommsServiceConnector
import javax.inject.Inject
import models.secureCommsServiceModels._
import models.secureMessageAlertModels.messageTypes._
import models._
import models.responseModels.SecureCommsServiceResponseModel
import org.bouncycastle.asn1.cms.RecipientEncryptedKey
import utils.LoggerUtil.logError
import utils.SecureCommsMessageParser
import utils.SecureCommsMessageParser._

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsService @Inject()(secureCommsServiceConnector: SecureCommsServiceConnector) {

  def sendSecureCommsMessage(workItem: SecureCommsMessageModel)
                           (implicit ec: ExecutionContext): Future[Either[ErrorModel, Boolean]] = {

    parseModel(workItem) match {
      case Left(error) =>  Future(Left(error))
      case Right(messageModel) => {
        buildSecureCommsServiceResponseModel(messageModel) match {
          case Left(a) => Future(Left(ErrorModel("", "")))
          case Right(b) => {
            secureCommsServiceConnector.sendMessage(b).map{
              case Right(response) => Right(true)
              case Left(error) => Left(error)
            }

          }
        }
      }
    }

    //secureCommsServiceConnector.sendMessage()

    //TODO: Need to wire up to the real connector when available and return the correct model instead of a boolean
    //Future(Right(true))
  }


  private def buildSecureCommsServiceResponseModel(messageModel: MessageModel) : Either[ErrorModel, SecureCommsServiceRequestModel] = {

    val model: SecureCommsServiceRequestModel = SecureCommsServiceRequestModel(
      ExternalRefModel(
        "fusRohID",
        "mtdp"
      ),
      RecipientModel(
        TaxIdentifierModel("key", "value"),
        NameModel("Lydia"),
        "swornToCarryYourBurdens@whiterun.tam"
      ),
      "some message type",
      "WE'RE TELLING YOU SOMETHING",
      "HERE HAVE SOME CONTENT"
    )

//    messageModel match {
//      case _:DeRegistrationModel => {
//        SecureCommsServiceRequestModel()
//      }
//
//      case _:PPOBChangeModel => {
//        SecureCommsServiceRequestModel()
//      }
//      case _:RepaymentsBankAccountChangeModel => {
//        SecureCommsServiceRequestModel()
//      }
//
//      case _:VATStaggerChangeModel => {
//        SecureCommsServiceRequestModel()
//      }
//
//      case _:EmailAddressChangeModel => {
//        SecureCommsServiceRequestModel()
//      }
//
//      case _:BusinessNameChangeModel => {
//        SecureCommsServiceRequestModel()
//      }

      case _ => Right(model)
    //}
  }

  //private def getHtmlView()

  private def parseMessageModel(workItem: SecureCommsMessageModel) : Either[ErrorModel, MessageModel] = {

    SecureCommsMessageParser.parseModel(workItem)

  }
}
