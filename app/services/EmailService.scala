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

import common.Constants.TemplateIdReadableNames._
import connectors.EmailConnector
import javax.inject.Inject
import models.ErrorModel
import models.emailRendererModels.EmailRequestModel
import models.responseModels.EmailRendererResponseModel
import models.secureMessageAlertModels.messageTypes.{EmailAddressChangeModel, MessageModel}

import scala.concurrent.{ExecutionContext, Future}

class EmailService @Inject()(emailRendererConnector: EmailConnector) {

  def sendEmailRequest(message: MessageModel)
                      (implicit ec: ExecutionContext): Future[Either[ErrorModel, EmailRendererResponseModel]] = {
    val mappedTemplateId = mapTemplateId(message.getTemplateId)
    toRequest(mappedTemplateId, message) match {
      case Right(request) => emailRendererConnector.sendEmailRequest(request)
      case Left(error) => Future.successful(Left(error))
    }
  }

  def mapTemplateId(input: String): String = {
    val allTemplateIds: Map[String, String] = Map(
      "VRT12C_SM1C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
      "VRT12C_SM3C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
      "VRT12C_SM5C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
      "VRT23C_SM7C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
      "VRT14C_SM2C" -> AGENT_NOTIFICATION_CHANGE_REJECTED,
      "VRT14C_SM4C" -> AGENT_NOTIFICATION_CHANGE_REJECTED,
      "VRT14C_SM6C" -> AGENT_NOTIFICATION_CHANGE_REJECTED,
      "VRT15C_SM8C" -> AGENT_NOTIFICATION_CHANGE_REJECTED,
      "VRT12A_SM9A" -> CLIENT_NOTIFICATION_SELF_CHANGE,
      "CC07C_SM11C" -> AGENT_NOTIFICATION_OPT_OUT,
      "VRT12C_SM14C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
      "VRT14C_SM15C" -> AGENT_NOTIFICATION_CHANGE_REJECTED,
      "VRT12C_SM12C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
      "VRT14C_SM13C" -> AGENT_NOTIFICATION_CHANGE_REJECTED
    )
    allTemplateIds(input)
  }

  def toRequest(templateId: String, messageModel: MessageModel): Either[ErrorModel, EmailRequestModel] =
    messageModel match {

      case model: EmailAddressChangeModel if templateId == CLIENT_NOTIFICATION_SELF_CHANGE =>
        val notificationDependentDetails: Map[String, String] = Map("recipientName_line1" -> model.getBusinessName)
        Right(EmailRequestModel(Seq(model.originalEmailAddress), templateId, notificationDependentDetails))

      case _ if templateId == AGENT_NOTIFICATION_CHANGE_ACCEPTED || templateId == AGENT_NOTIFICATION_CHANGE_REJECTED ||
        templateId == AGENT_NOTIFICATION_OPT_OUT =>
        val notificationDependentDetails: Map[String, String] = Map(
          "transactorName" -> messageModel.getTransactorDetails.transactorName,
          "clientName" -> messageModel.getBusinessName,
          "clientVrn" -> messageModel.getVrn
        )
        Right(EmailRequestModel(
          Seq(messageModel.getTransactorDetails.transactorEmail), templateId, notificationDependentDetails
        ))
      case _ => Left(ErrorModel("ERROR_CREATING_REQUEST", s"Template ID '$templateId' is not supported."))
    }
}
