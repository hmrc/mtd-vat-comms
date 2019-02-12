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
import connectors.EmailRendererConnector
import models.ErrorModel
import models.emailRendererModels.EmailRendererRequestModel
import models.responseModels.EmailRendererResponseModel
import models.secureCommsModels.messageTypes.MessageModel
import models.secureCommsModels.{CustomerModel, PreferencesModel, TransactorModel}
import org.scalamock.scalatest.MockFactory
import play.api.http.Status.ACCEPTED
import utils.Constants.ChannelPreferences.PAPER
import utils.Constants.EmailStatus.VERIFIED
import utils.Constants.FormatPreferences.TEXT
import utils.Constants.LanguagePreferences.ENGLISH
import utils.Constants.NotificationPreference.EMAIL
import utils.Constants.TemplateIdReadableNames._

import scala.concurrent.{ExecutionContext, Future}

class EmailRendererServiceSpec extends BaseSpec with MockFactory {
  val mockConnector: EmailRendererConnector = mock[EmailRendererConnector]
  val service: EmailRendererService = new EmailRendererService(mockConnector)

  "sendEmailRequest" should {
    "return an EmailRendererResponseModel when successful" in {
      val messageModel: MessageModel = new MessageModel("VRT12B", "123123123", "AID_32I1", "FUS ROH DAH",
        TransactorModel("some@thing.ha.ha", "SomeThing"), CustomerModel("cus@tom.e.r", VERIFIED), PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT))

      val expectedAgentRequest = EmailRendererRequestModel(
        Seq(messageModel.getTransactorDetails.transactorEmail),
        "newMessageAlert_VRT12B",
        Map(
          "transactorName" -> messageModel.getTransactorDetails.transactorName,
          "clientName" -> messageModel.getBusinessName,
          "clientVrn" -> messageModel.getVrn
        )
      )

      val expectedClientRequest = EmailRendererRequestModel(
        Seq(messageModel.getCustomerDetails.customerEmail),
        "newMessageAlert_VRT1214C",
        Map(
          "recipientName_line1" -> messageModel.getBusinessName
        )
      )

      (mockConnector.sendEmailRequest(_: EmailRendererRequestModel)(_: ExecutionContext))
        .expects(expectedClientRequest, *)
        .returning(Future.successful(Right(EmailRendererResponseModel(ACCEPTED))))

      (mockConnector.sendEmailRequest(_: EmailRendererRequestModel)(_: ExecutionContext))
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
        "VRT12B" -> Seq("VRT1214C", "VRT12B"),
        "VRT14B" -> Seq("VRT1214C", "VRT14B"),
        "VRT15B" -> Seq("VRT1214C", "VRT14B"),
        "VRT23B" -> Seq("VRT1214C", "VRT12B"),
        "VRT12A" -> Seq("VRT1214A"),
        "VRT14A" -> Seq("VRT1214A"),
        "VRT15A" -> Seq("VRT1214A"),
        "VRT23A" -> Seq("VRT1214A")
      ).mapValues(_.map(value => "newMessageAlert_" + value)).foreach { mapping =>
        s"incoming id is ${mapping._1}" in {
          service.mapTemplateId(mapping._1) shouldBe mapping._2
        }
      }
    }
  }

  "toRequest" should {
    "return an EmailRendererRequestModel" when {
      "a valid client template id is used" in {
        val templateId = CLIENT_NOTIFICATION_SELF_CHANGE
        val modelTemplateId = "VRT12A"
        val mModel = new MessageModel(modelTemplateId, "1234567890", "ALSKDLASKKDLAKS", "FusRohDheli",
          TransactorModel("", ""), CustomerModel("some@email.co.uk", VERIFIED), PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT))
        val params = Map(
          "recipientName_line1" -> mModel.getBusinessName
        )

        service.toRequest(templateId, mModel) shouldBe Right(EmailRendererRequestModel(Seq("some@email.co.uk"), templateId, params))
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

        service.toRequest(templateId, mModel) shouldBe Right(EmailRendererRequestModel(Seq("agent@email.me.no"), templateId, params))
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
