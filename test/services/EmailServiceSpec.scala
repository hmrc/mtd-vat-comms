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

import base.BaseSpec
import connectors.EmailConnector
import models.ErrorModel
import models.emailRendererModels.EmailRequestModel
import models.responseModels.EmailRendererResponseModel
import models.secureMessageAlertModels.messageTypes.MessageModel
import models.secureMessageAlertModels.{CustomerModel, PreferencesModel, TransactorModel}
import org.scalamock.scalatest.MockFactory
import play.api.http.Status.ACCEPTED
import common.Constants.ChannelPreferences.PAPER
import common.Constants.EmailStatus.VERIFIED
import common.Constants.FormatPreferences.TEXT
import common.Constants.LanguagePreferences.ENGLISH
import common.Constants.NotificationPreference.EMAIL
import common.Constants.TemplateIdReadableNames._

import scala.concurrent.{ExecutionContext, Future}

class EmailServiceSpec extends BaseSpec with MockFactory {
  val mockConnector: EmailConnector = mock[EmailConnector]
  val service: EmailService = new EmailService(mockConnector)

  "sendEmailRequest" should {
    "return an EmailRendererResponseModel when successful" in {
      val messageModel: MessageModel = new MessageModel("VRT12C_SM1C", "123123123", "AID_32I1", "FUS ROH DAH",
        TransactorModel("some@thing.ha.ha", "SomeThing"), CustomerModel("cus@tom.e.r", VERIFIED), PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT))

      val expectedAgentRequest = EmailRequestModel(
        Seq(messageModel.getTransactorDetails.transactorEmail),
        "newMessageAlert_VRT12B",
        Map(
          "transactorName" -> messageModel.getTransactorDetails.transactorName,
          "clientName" -> messageModel.getBusinessName,
          "clientVrn" -> messageModel.getVrn
        )
      )

      val expectedClientRequest = EmailRequestModel(
        Seq(messageModel.getCustomerDetails.customerEmail),
        "newMessageAlert_VRT1214C",
        Map(
          "recipientName_line1" -> messageModel.getBusinessName
        )
      )

      (mockConnector.sendEmailRequest(_: EmailRequestModel)(_: ExecutionContext))
        .expects(expectedClientRequest, *)
        .returning(Future.successful(Right(EmailRendererResponseModel(ACCEPTED))))

      (mockConnector.sendEmailRequest(_: EmailRequestModel)(_: ExecutionContext))
        .expects(expectedAgentRequest, *)
        .returning(Future.successful(Right(EmailRendererResponseModel(ACCEPTED))))

      val result: Either[ErrorModel, EmailRendererResponseModel] = await(service.sendEmailRequest(messageModel))
      result shouldBe Right(EmailRendererResponseModel(ACCEPTED))
    }
    "return an ErrorModel when unsuccessful" in {}
  }

  "mapTemplateId" should {
    "map incoming template IDs to the correct outgoing ID(s)" when {
      Map(
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
      ).foreach { mapping =>
        s"incoming id is ${mapping._1}" in {
          service.mapTemplateId(mapping._1) shouldBe mapping._2
        }
      }
    }
  }

  "toRequest" should {
    "return an EmailRequestModel" when {
      "a valid client template id is used" in {
        val templateId = CLIENT_NOTIFICATION_SELF_CHANGE
        val modelTemplateId = "VRT12A"
        val mModel = new MessageModel(modelTemplateId, "1234567890", "ALSKDLASKKDLAKS", "FusRohDheli",
          TransactorModel("", ""), CustomerModel("some@email.co.uk", VERIFIED), PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT))
        val params = Map(
          "recipientName_line1" -> mModel.getBusinessName
        )

        service.toRequest(templateId, mModel) shouldBe Right(EmailRequestModel(Seq("some@email.co.uk"), templateId, params))
      }
      "a valid agent template id is used" in {
        val templateId = AGENT_NOTIFICATION_CHANGE_ACCEPTED
        val modelTemplateId = "VRT12B"
        val mModel = new MessageModel(modelTemplateId, "1234567890", "ALSKDLASKKDLAKS", "FusRohDheli",
          TransactorModel("agent@email.me.no", "Alduin"), CustomerModel("some@email.co.uk", VERIFIED), PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT))
        val params = Map(
          "transactorName" -> mModel.getTransactorDetails.transactorName,
          "clientName" -> mModel.getBusinessName,
          "clientVrn" -> mModel.getVrn
        )

        service.toRequest(templateId, mModel) shouldBe Right(EmailRequestModel(Seq("agent@email.me.no"), templateId, params))
      }
    }
    "return an ErrorModel" when {
      "an invalid TemplateID is used" in {
        val templateId = "newMessageAlert_VRT1214B"
        val modelTemplateId = "VRT12O"
        val mModel = new MessageModel(modelTemplateId, "1234567890", "ALSKDLASKKDLAKS", "FusRohDheli",
          TransactorModel("agent@email.me.no", "Alduin"), CustomerModel("some@email.co.uk", VERIFIED), PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT))
        val params = Map(
          "transactorName" -> mModel.getTransactorDetails.transactorName,
          "clientName" -> mModel.getBusinessName,
          "clientVrn" -> mModel.getVrn
        )

        service.toRequest(templateId, mModel) shouldBe Left(ErrorModel("ERROR_CREATING_REQUEST", "newMessageAlert_VRT1214B (of class java.lang.String)"))
      }
    }
  }
}
