/*
 * Copyright 2023 HM Revenue & Customs
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

package connectors

import base.BaseSpec
import common.Constants.ChannelPreferences.PAPER
import common.Constants.EmailStatus.VERIFIED
import common.Constants.FormatPreferences.TEXT
import common.Constants.LanguagePreferences.ENGLISH
import common.Constants.NotificationPreference.EMAIL
import mocks.MockHttpClient
import models.ErrorModel
import models.emailRendererModels.EmailRequestModel
import models.responseModels.EmailRendererResponseModel
import models.secureMessageAlertModels.{CustomerModel, PreferencesModel, TransactorModel}
import models.secureMessageAlertModels.messageTypes.MessageModel
import play.api.http.Status.BAD_GATEWAY
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.RequestTimeoutException

import scala.concurrent.Future

class EmailConnectorSpec extends BaseSpec with MockHttpClient {

  val connector = new EmailConnector(mockHttpClient, mockAppConfig)

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
    Seq(messageModel.transactorDetails.transactorEmail),
    "newMessageAlert_VRT12B",
    Map(
      "transactorName" -> messageModel.transactorDetails.transactorName,
      "clientName" -> messageModel.businessName,
      "clientVrn" -> messageModel.vrn
    )
  )

  type EmailResponse = Either[ErrorModel, EmailRendererResponseModel]

  "EmailConnector .sendEmailRequest" should {

    "return a 502 error when there is a HTTP exception" in {
      val exception = new RequestTimeoutException("Request timed out!!!")
      mockHttpPost[EmailRequestModel, EmailResponse](Future.failed(exception))
      val result = connector.sendEmailRequest(emailRequestModel)
      await(result) shouldBe Left(ErrorModel(BAD_GATEWAY.toString, exception.message))
    }
  }
}
