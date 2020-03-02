/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package testOnly.controllers

import base.BaseSpec
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.Result
import repositories.SecureMessageQueueRepository
import services.SecureMessageQueuePollingService

import scala.concurrent.Future

class SecureMessageQueueControllerSpec extends BaseSpec with MockitoSugar {

  val repository: SecureMessageQueueRepository = mock[SecureMessageQueueRepository]
  val scheduler: SecureMessageQueuePollingService = mock[SecureMessageQueuePollingService]
  val controller = new SecureMessageQueueController(repository, scheduler)
  val recordCount = 99

  "The count action" should {

    "return the total number of records in the SecureMessageQueue database" in {
      when(repository.count(ec)) thenReturn Future.successful(recordCount)
      lazy val result: Future[Result] = controller.count(request)

      await(bodyOf(result)) shouldBe recordCount.toString
    }
  }
}
