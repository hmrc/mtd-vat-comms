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

package services

import base.BaseSpec
import config.AppConfig
import models.SecureCommsMessageModel
import org.mockito.Mockito
import org.mockito.Mockito.{timeout, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import utils.SecureCommsMessageTestData.Responses

import scala.concurrent.Future

class EmailMessageQueuePollingServiceSpec extends BaseSpec with MockitoSugar {

  val timeoutForTest: Int = 4000
  val mockConfig: AppConfig = mock[AppConfig]

  "EmailMessageQueuePollingService" should {

    "be named correctly" in new TestSetup {
      val queuePollingService: EmailMessageQueuePollingService =
        new EmailMessageQueuePollingService(actorSystem, mockConfig, mockEmailMessageService)

      queuePollingService.name shouldBe "EmailMessageQueuePollingService"
    }

    "poll the queue twice in the given test time period when the toggle is on" in new TestSetup(true) {
      new EmailMessageQueuePollingService(actorSystem, mockConfig, mockEmailMessageService)

      verify(mockEmailMessageService, timeout(timeoutForTest).times(2)).retrieveWorkItems
    }

    "not poll the queue when the toggle is off" in new TestSetup {
      new EmailMessageQueuePollingService(actorSystem, mockConfig, mockEmailMessageService)

      verify(mockEmailMessageService, Mockito.after(timeoutForTest).never()).retrieveWorkItems
    }
  }

  class TestSetup(pollingEnabled: Boolean = false) {
    val exampleSecureCommsMessageModel: SecureCommsMessageModel = Responses.expectedResponseEverything
    val mockEmailMessageService: EmailMessageService = mock[EmailMessageService]

    when(mockConfig.queuePollingWaitTime).thenReturn(1)
    when(mockConfig.pollingToggle).thenReturn(pollingEnabled)
    when(mockConfig.initialWaitTime).thenReturn(1)

    when(mockEmailMessageService.retrieveWorkItems).thenReturn(Future.successful(Seq(exampleSecureCommsMessageModel)))
  }
}
