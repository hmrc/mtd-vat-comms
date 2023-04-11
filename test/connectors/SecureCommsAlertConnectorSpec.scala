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
import mocks.MockHttpClient
import models.ErrorModel
import models.responseModels.SecureCommsResponseModel
import play.api.http.Status.BAD_GATEWAY
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.RequestTimeoutException

import scala.concurrent.Future

class SecureCommsAlertConnectorSpec extends BaseSpec with MockHttpClient {

  val connector = new SecureCommsAlertConnector(mockHttpClient, mockAppConfig)
  type SecureCommsAlertResponse = Either[ErrorModel, SecureCommsResponseModel]

  "SecureCommsAlertConnector .getSecureCommsMessage" should {

    "return a 502 error when there is a HTTP exception" in {
      val exception = new RequestTimeoutException("Request timed out!!!")
      mockHttpGet[SecureCommsAlertResponse](Future.failed(exception))
      val result = connector.getSecureCommsMessage("VATC", "123456789", "2019-01-01T09:00:00Z")
      await(result) shouldBe Left(ErrorModel(BAD_GATEWAY.toString, exception.message))
    }
  }

}
