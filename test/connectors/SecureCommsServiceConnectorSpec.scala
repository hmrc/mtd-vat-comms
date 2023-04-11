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
import common.Constants.SecureCommsServiceFieldValues.MDTP
import mocks.MockHttpClient
import models.ErrorModel
import models.secureCommsServiceModels.{ExternalRefModel, NameModel, RecipientModel, SecureCommsServiceRequestModel, TaxIdentifierModel}
import play.api.http.Status.BAD_GATEWAY
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.RequestTimeoutException

import scala.concurrent.Future

class SecureCommsServiceConnectorSpec extends BaseSpec with MockHttpClient {

  val connector = new SecureCommsServiceConnector(mockHttpClient, mockAppConfig)
  type SecureCommsResponse = Either[ErrorModel, Boolean]

  val externalRefModel: ExternalRefModel = ExternalRefModel("testId", MDTP)
  val recipientModel: RecipientModel = RecipientModel(
    TaxIdentifierModel("key", "value"),
    NameModel("testName"),
    "test@email.com"
  )
  val secureCommsServiceRequestModel: SecureCommsServiceRequestModel = new SecureCommsServiceRequestModel(
    externalRefModel,
    recipientModel,
    "testMessageType",
    "testSubject",
    "testContent"
  )

  "SecureCommsServiceConnector .sendMessage" should {

    "return a 502 error when there is a HTTP exception" in {
      val exception = new RequestTimeoutException("Request timed out!!!")
      mockHttpPost[SecureCommsServiceRequestModel, SecureCommsResponse](Future.failed(exception))
      val result = connector.sendMessage(secureCommsServiceRequestModel)
      await(result) shouldBe Left(ErrorModel(BAD_GATEWAY.toString, exception.message))
    }
  }
}
