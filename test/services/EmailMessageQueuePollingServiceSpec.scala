/*
 * Copyright 2020 HM Revenue & Customs
 *
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
  val mockAppConfig: AppConfig = mock[AppConfig]

  "EmailMessageQueuePollingService" should {

    "be named correctly" in new TestSetup {
      val queuePollingService: EmailMessageQueuePollingService = new EmailMessageQueuePollingService(
        actorSystem, mockAppConfig, mockEmailMessageService)

      queuePollingService.name shouldBe "EmailMessageQueuePollingService"
    }

    "poll the queue twice in the given test time period when the toggle is on" in new TestSetup(true) {
      new EmailMessageQueuePollingService(
        actorSystem, mockAppConfig, mockEmailMessageService)

      verify(mockEmailMessageService, timeout(timeoutForTest).times(2)).retrieveWorkItems
    }

    "not poll the queue when the toggle is off" in new TestSetup {
      new EmailMessageQueuePollingService(
        actorSystem, mockAppConfig, mockEmailMessageService)

      verify(mockEmailMessageService, Mockito.after(timeoutForTest).never()).retrieveWorkItems
    }
  }

  class TestSetup(pollingEnabled: Boolean = false) {
    val exampleSecureCommsMessageModel: SecureCommsMessageModel = Responses.expectedResponseEverything
    val mockEmailMessageService: EmailMessageService = mock[EmailMessageService]

    when(mockAppConfig.queuePollingWaitTime).thenReturn(1)
    when(mockAppConfig.pollingToggle).thenReturn(pollingEnabled)
    when(mockAppConfig.initialWaitTime).thenReturn(1)

    when(mockEmailMessageService.retrieveWorkItems).thenReturn(Future.successful(Seq(exampleSecureCommsMessageModel)))
  }

}
