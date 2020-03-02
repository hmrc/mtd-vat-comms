/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import base.BaseSpec
import common.ApiConstants._
import common.VatChangeEventConstants._
import mocks.MockCommsEventService
import models.VatChangeEvent
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NO_CONTENT}
import play.api.libs.json.JsObject
import play.api.mvc.Result

import scala.concurrent.Future

class ContactNumbersControllerSpec extends BaseSpec with MockCommsEventService {

  val controller = new ContactNumbersController(mockCommsEventService)

  val testRequestJson: JsObject        = vatChangeEventJson("Contact Numbers Change")
  val testRequestModel: VatChangeEvent = vatChangeEventModel("Contact Numbers Change")

  "The handleEvent action" when {

    "valid JSON is received" when {

      "the Contact Numbers Change event was successfully added to the queue" should {

        "return 204" in {
          mockQueueRequest(testRequestModel)(Future.successful(true))
          val result: Result = controller.handleEvent(request.withJsonBody(testRequestJson))

          status(result) shouldBe NO_CONTENT
        }
      }

      "the Contact Numbers Change event was unsuccessfully added to the queue" should {

        "return 500" in {
          mockQueueRequest(testRequestModel)(Future.successful(false))
          val result: Result = controller.handleEvent(request.withJsonBody(testRequestJson))

          status(result) shouldBe INTERNAL_SERVER_ERROR
        }
      }
    }

    "invalid JSON is received" should {

      "return 400" in {
        val result: Result = controller.handleEvent(request.withJsonBody(invalidJsonRequest))
        status(result) shouldBe BAD_REQUEST
      }

      "return JSON describing the error" in {
        val result: Result = controller.handleEvent(request.withJsonBody(invalidJsonRequest))
        jsonBodyOf(result) shouldBe invalidJsonResponse
      }
    }

    "something other than JSON is received" should {

      "return 400" in {
        val result: Result = controller.handleEvent(request.withBody(invalidRequestBody))
        status(result) shouldBe BAD_REQUEST
      }

      "return JSON describing the error" in {
        val result: Result = controller.handleEvent(request.withBody(invalidRequestBody))
        jsonBodyOf(result) shouldBe invalidFormatResponse
      }
    }
  }
}
