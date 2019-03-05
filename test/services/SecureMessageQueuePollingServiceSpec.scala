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

import akka.actor.ActorSystem
import base.BaseSpec
import mocks.MockAppConfig
import models.SecureCommsMessageModel
import org.mockito.Mockito
import org.mockito.Mockito.{timeout, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import utils.SecureCommsMessageTestData.Responses.expectedResponsePPOBChange

import scala.concurrent.Future

class SecureMessageQueuePollingServiceSpec extends BaseSpec with MockitoSugar {

  val timeoutForTest: Int = 4000

  "SecureMessageQueuePollingService" should {

    "be named correctly" in new TestSetup {

      val queuePollingService: SecureMessageQueuePollingService = new SecureMessageQueuePollingService(
        actorSystem, mockAppConfig, mockSecureMessageService)

      queuePollingService.name shouldBe "SecureMessageQueuePollingService"
    }

    "poll the queue twice in the given test time period when the toggle is on" in new TestSetup {

      when(mockSecureMessageService.retrieveWorkItems).thenReturn(Future.successful(Seq(exampleSecureCommsMessageModel)))

      new SecureMessageQueuePollingService(
        actorSystem, new MockAppConfig(Configuration(), pollingToggle = true), mockSecureMessageService)

      verify(mockSecureMessageService, timeout(timeoutForTest).times(2)).retrieveWorkItems
    }

    "not poll the queue when the toggle is off" in new TestSetup {

      new SecureMessageQueuePollingService(
        actorSystem, mockAppConfig, mockSecureMessageService)

      verify(mockSecureMessageService, Mockito.after(timeoutForTest).never()).retrieveWorkItems
    }
  }

  trait TestSetup {
    val actorSystem: ActorSystem = app.injector.instanceOf[ActorSystem]
    val exampleSecureCommsMessageModel: SecureCommsMessageModel = expectedResponsePPOBChange
    val mockSecureMessageService: SecureMessageService = mock[SecureMessageService]
  }

}
