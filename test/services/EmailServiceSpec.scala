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

import base.BaseSpec
import common.Constants.ChannelPreferences.PAPER
import common.Constants.EmailStatus.VERIFIED
import common.Constants.FormatPreferences.TEXT
import common.Constants.LanguagePreferences.ENGLISH
import common.Constants.NotificationPreference.EMAIL
import common.Constants.TemplateIdReadableNames._
import connectors.EmailConnector
import models.ErrorModel
import models.emailRendererModels.EmailRequestModel
import models.responseModels.EmailRendererResponseModel
import models.secureMessageAlertModels.messageTypes.{EmailAddressChangeModel, MessageModel}
import models.secureMessageAlertModels.{CustomerModel, PreferencesModel, TransactorModel}
import org.scalamock.scalatest.MockFactory
import play.api.http.Status.ACCEPTED
import play.api.test.Helpers.{await, defaultAwaitTimeout}

import scala.concurrent.{ExecutionContext, Future}

class EmailServiceSpec extends BaseSpec with MockFactory {

  val mockConnector: EmailConnector = mock[EmailConnector]
  val service: EmailService = new EmailService(mockConnector)

  val messageModel: MessageModel = new MessageModel(
    "VRT12C_SM1C",
    "123123123",
    "AID_32I1",
    "testBusinessName",
    TransactorModel("test@email.com", "test"),
    CustomerModel("cus@tom.e.r", VERIFIED),
    PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
  )

  val emailRequestModel: EmailRequestModel = EmailRequestModel(
    Seq(messageModel.getTransactorDetails.transactorEmail),
    "newMessageAlert_VRT12B",
    Map(
      "transactorName" -> messageModel.getTransactorDetails.transactorName,
      "clientName" -> messageModel.getBusinessName,
      "clientVrn" -> messageModel.getVrn
    )
  )

  val errorModel: ErrorModel = ErrorModel("ERROR_CREATING_REQUEST", "Oh no")

  "sendEmailRequest" should {

    "return an EmailRendererResponseModel when successful" in {

      (mockConnector.sendEmailRequest(_: EmailRequestModel)(_: ExecutionContext))
        .expects(emailRequestModel, *)
        .returning(Future.successful(Right(EmailRendererResponseModel(ACCEPTED))))

      val result: Either[ErrorModel, EmailRendererResponseModel] = await(service.sendEmailRequest(messageModel))
      result shouldBe Right(EmailRendererResponseModel(ACCEPTED))
    }

    "return an ErrorModel when unsuccessful" in {

      (mockConnector.sendEmailRequest(_: EmailRequestModel)(_: ExecutionContext))
        .expects(emailRequestModel, *)
        .returning(Future.successful(Left(errorModel)))

      val result = await(service.sendEmailRequest(messageModel))
      result shouldBe Left(errorModel)
    }
  }

  "mapTemplateId" should {

    "map incoming template IDs to the correct outgoing ID(s)" when {
      Map(
        "VRT12C_SM1C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
        "VRT12C_SM3C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
        "VRT12C_SM5C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
        "VRT23C_SM7C" -> AGENT_NOTIFICATION_CHANGE_ACCEPTED,
        "VRT14C_SM2C" -> AGENT_NOTIFICATION_CHANGE_REJECTED,
        "VRT14C_SM4C" -> AGENT_NOTIFICATION_CHANGE_REJECTED,
        "VRT14C_SM6C" -> AGENT_NOTIFICATION_CHANGE_REJECTED,
        "VRT15C_SM8C" -> AGENT_NOTIFICATION_CHANGE_REJECTED,
        "VRT12A_SM9A" -> CLIENT_NOTIFICATION_SELF_CHANGE,
        "CC07C_SM11C" -> AGENT_NOTIFICATION_OPT_OUT
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
        val mModel = new EmailAddressChangeModel(
          "VRT12A_SM9A",
          "1234567890",
          "ALSKDLASKKDLAKS",
          "testTransactorName",
          TransactorModel("", ""),
          CustomerModel("some@email.co.uk", VERIFIED),
          PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT),
          "mynewemail@vat.change"
        )
        val params = Map("recipientName_line1" -> mModel.getBusinessName)

        service.toRequest(templateId, mModel) shouldBe Right(EmailRequestModel(Seq("mynewemail@vat.change"), templateId, params))
      }

      "a valid agent template id is used" in {
        val templateId = AGENT_NOTIFICATION_CHANGE_ACCEPTED
        val mModel = new MessageModel(
          "VRT12C_SM1C",
          "1234567890",
          "ALSKDLASKKDLAKS",
          "testTransactorName",
          TransactorModel("agent@email.me.no", "Alduin"),
          CustomerModel("some@email.co.uk", VERIFIED),
          PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
        )
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
        val mModel = new MessageModel(
          "VRT12C_SM1C",
          "1234567890",
          "ALSKDLASKKDLAKS",
          "testTransactorNa",
          TransactorModel("agent@email.me.no", "Alduin"),
          CustomerModel("some@email.co.uk", VERIFIED),
          PreferencesModel(EMAIL, PAPER, ENGLISH, TEXT)
        )

        service.toRequest(templateId, mModel) shouldBe
          Left(ErrorModel("ERROR_CREATING_REQUEST", "Template ID 'newMessageAlert_VRT1214B' is not supported."))
      }
    }
  }
}
