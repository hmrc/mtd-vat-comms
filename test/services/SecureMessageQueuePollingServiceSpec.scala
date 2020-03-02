/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import akka.actor.ActorSystem
import base.BaseSpec
import config.AppConfig
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
  val mockAppConfig = mock[AppConfig]

  "SecureMessageQueuePollingService" should {

    "be named correctly" in new TestSetup {
      val queuePollingService: SecureMessageQueuePollingService = new SecureMessageQueuePollingService(
        actorSystem, mockAppConfig, mockSecureMessageService)

      queuePollingService.name shouldBe "SecureMessageQueuePollingService"
    }

    "poll the queue twice in the given test time period when the toggle is on" in new TestSetup(true){
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

  class TestSetup(pollingEnabled: Boolean = false) {
    val exampleSecureCommsMessageModel: SecureCommsMessageModel = expectedResponsePPOBChange
    val mockSecureMessageService: SecureMessageService = mock[SecureMessageService]

    when(mockAppConfig.queuePollingWaitTime).thenReturn(1)
    when(mockAppConfig.pollingToggle).thenReturn(pollingEnabled)
    when(mockAppConfig.initialWaitTime).thenReturn(1)

    when(mockSecureMessageService.retrieveWorkItems).thenReturn(Future.successful(Seq(exampleSecureCommsMessageModel)))
  }

}
