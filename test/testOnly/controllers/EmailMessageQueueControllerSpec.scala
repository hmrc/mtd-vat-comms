/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package testOnly.controllers

import base.BaseSpec
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.Result
import repositories.EmailMessageQueueRepository
import services.EmailMessageQueuePollingService

import scala.concurrent.Future

class EmailMessageQueueControllerSpec extends BaseSpec with MockitoSugar {

  val repository: EmailMessageQueueRepository = mock[EmailMessageQueueRepository]
  val scheduler: EmailMessageQueuePollingService = mock[EmailMessageQueuePollingService]
  val controller = new EmailMessageQueueController(repository, scheduler)
  val recordCount = 99

  "The count action" should {

    "return the total number of records in the EmailMessageQueue database" in {
      when(repository.count(ec)) thenReturn Future.successful(recordCount)
      lazy val result: Future[Result] = controller.count(request)

      await(bodyOf(result)) shouldBe recordCount.toString
    }
  }
}
