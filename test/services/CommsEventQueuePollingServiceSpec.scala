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
import common.ApiConstants.vatChangeEventModel
import mocks.MockAppConfig
import models.VatChangeEvent
import org.mockito.Mockito
import org.mockito.Mockito.{timeout, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration

import scala.concurrent.Future

class CommsEventQueuePollingServiceSpec extends BaseSpec with MockitoSugar {

  val timeoutForTest: Int = 4000

  "CommsEventQueuePollingService" should {

    "be named correctly" in new TestSetup {

      val queuePollingService: CommsEventQueuePollingService = new CommsEventQueuePollingService(
        actorSystem, mockAppConfig, mockCommsEventService)

      queuePollingService.name shouldBe "CommsEventQueuePollingService"
    }

    "poll the queue twice in the given test time period when the toggle is on" in new TestSetup {

      when(mockCommsEventService.retrieveWorkItems).thenReturn(Future.successful(Seq(exampleVatChangeEvent)))

      new CommsEventQueuePollingService(
        actorSystem, new MockAppConfig(Configuration(), pollingToggle = true), mockCommsEventService)

      verify(mockCommsEventService, timeout(timeoutForTest).times(2)).retrieveWorkItems
    }

    "not poll the queue when the toggle is off" in new TestSetup {

      new CommsEventQueuePollingService(
        actorSystem, mockAppConfig, mockCommsEventService)

      verify(mockCommsEventService, Mockito.after(timeoutForTest).never()).retrieveWorkItems
    }
  }

  trait TestSetup {
    val actorSystem: ActorSystem = app.injector.instanceOf[ActorSystem]
    val exampleVatChangeEvent: VatChangeEvent = vatChangeEventModel("Email Address Change")
    val mockCommsEventService: CommsEventService = mock[CommsEventService]
  }

}
