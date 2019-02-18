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

import connectors.EmailConnector
import javax.inject.Inject
import models.ErrorModel
import models.emailRendererModels.EmailRequestModel
import models.responseModels.EmailRendererResponseModel
import models.secureMessageAlertModels.messageTypes.MessageModel
import play.api.http.Status.ACCEPTED
import common.Constants.TemplateIdReadableNames._

import scala.concurrent.{ExecutionContext, Future}

class EmailService @Inject()(emailRendererConnector: EmailConnector) {

  def sendEmailRequest(message: MessageModel)(implicit ec: ExecutionContext): Future[Either[ErrorModel, EmailRendererResponseModel]] = {
    Future.sequence(mapTemplateId(message.getTemplateId).map { templateId =>
      toRequest(templateId, message) match {
        case Right(request) => emailRendererConnector.sendEmailRequest(request)
        case Left(error) => Future.successful(Left(error))
      }
    }).map {
      _.foldRight[Either[ErrorModel, EmailRendererResponseModel]](Right(EmailRendererResponseModel(ACCEPTED))) {
        (currentIndex: Either[ErrorModel, EmailRendererResponseModel], lastIndex: Either[ErrorModel, EmailRendererResponseModel]) =>
          (currentIndex.isRight, lastIndex.isRight) match {
            case (false, false) =>
              Left(ErrorModel(
                currentIndex.left.get.code,
                currentIndex.left.get.body + "\n" + lastIndex.left.get.body
              ))
            case (true, false) => lastIndex
            case (_, true) => currentIndex
          }
      }
    }
  }

  def mapTemplateId(input: String): Seq[String] = {
    val allTemplateIds: Map[String, Seq[String]] = Map(
      "VRT12C_SM1C" -> Seq(CLIENT_NOTIFICATION_AGENT_CHANGE, AGENT_NOTIFICATION_CHANGE_ACCEPTED),
      "VRT12C_SM3C" -> Seq(CLIENT_NOTIFICATION_AGENT_CHANGE, AGENT_NOTIFICATION_CHANGE_ACCEPTED),
      "VRT12C_SM5C" -> Seq(CLIENT_NOTIFICATION_AGENT_CHANGE, AGENT_NOTIFICATION_CHANGE_ACCEPTED),
      "VRT23C_SM7C" -> Seq(CLIENT_NOTIFICATION_AGENT_CHANGE, AGENT_NOTIFICATION_CHANGE_ACCEPTED),
      "VRT14C_SM2C" -> Seq(CLIENT_NOTIFICATION_AGENT_CHANGE, AGENT_NOTIFICATION_CHANGE_REJECTED),
      "VRT14C_SM4C" -> Seq(CLIENT_NOTIFICATION_AGENT_CHANGE, AGENT_NOTIFICATION_CHANGE_REJECTED),
      "VRT14C_SM6C" -> Seq(CLIENT_NOTIFICATION_AGENT_CHANGE, AGENT_NOTIFICATION_CHANGE_REJECTED),
      "VRT15C_SM8C" -> Seq(CLIENT_NOTIFICATION_AGENT_CHANGE, AGENT_NOTIFICATION_CHANGE_REJECTED),
      "VRT12A_SM1A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE),
      "VRT14A_SM2A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE),
      "VRT12A_SM3A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE),
      "VRT14A_SM4A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE),
      "VRT12A_SM5A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE),
      "VRT14A_SM6A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE),
      "VRT23A_SM7A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE),
      "VRT15A_SM8A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE),
      "VRT12A_SM9A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE),
      "VRT14A_SM10A" -> Seq(CLIENT_NOTIFICATION_SELF_CHANGE)
    )
    allTemplateIds(input)
  }

  def toRequest(templateId: String, messageModel: MessageModel): Either[ErrorModel, EmailRequestModel] = {
    try {

      val notificationDependentDetails: (String, Map[String, String]) = templateId match {
        case CLIENT_NOTIFICATION_SELF_CHANGE | CLIENT_NOTIFICATION_AGENT_CHANGE =>
          (
            messageModel.getCustomerDetails.customerEmail,
            Map("recipientName_line1" -> messageModel.getBusinessName)
          )
        case AGENT_NOTIFICATION_CHANGE_ACCEPTED | AGENT_NOTIFICATION_CHANGE_REJECTED =>
          (
            messageModel.getTransactorDetails.transactorEmail,
            Map(
              "transactorName" -> messageModel.getTransactorDetails.transactorName,
              "clientName" -> messageModel.getBusinessName,
              "clientVrn" -> messageModel.getVrn
            )
          )
      }

      Right(EmailRequestModel(Seq(notificationDependentDetails._1), templateId, notificationDependentDetails._2))
    } catch {
      case error: Throwable => Left(ErrorModel("ERROR_CREATING_REQUEST", error.getMessage))
    }
  }
}
