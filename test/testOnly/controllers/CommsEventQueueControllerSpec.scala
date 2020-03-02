/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package testOnly.controllers

import base.BaseSpec
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.Result
import repositories.CommsEventQueueRepository
import services.CommsEventQueuePollingService

import scala.concurrent.Future

class CommsEventQueueControllerSpec extends BaseSpec with MockitoSugar {

  val repository: CommsEventQueueRepository = mock[CommsEventQueueRepository]
  val scheduler: CommsEventQueuePollingService = mock[CommsEventQueuePollingService]
  val controller = new CommsEventQueueController(repository, scheduler)
  val recordCount = 99

  "The count action" should {

    "return the total number of records in the CommsEventQueue database" in {
      when(repository.count(ec)) thenReturn Future.successful(recordCount)
      lazy val result: Future[Result] = controller.count(request)

      await(bodyOf(result)) shouldBe recordCount.toString
    }
  }
}
