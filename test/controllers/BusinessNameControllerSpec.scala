/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import base.BaseSpec
import common.ApiConstants._
import common.VatChangeEventConstants._
import models.VatChangeEvent
import play.api.http.Status.{BAD_REQUEST, NO_CONTENT}
import play.api.libs.json.JsObject
import play.api.mvc.Result

class BusinessNameControllerSpec extends BaseSpec {

  val controller = new BusinessNameController()

  val testRequestJson: JsObject        = vatChangeEventJson("Business Name Change")
  val testRequestModel: VatChangeEvent = vatChangeEventModel("Business Name Change")

  "The handleEvent action" when {

    "valid JSON is received" should {

      "return 204" in {
        val result: Result = controller.handleEvent(request.withJsonBody(testRequestJson))
        status(result) shouldBe NO_CONTENT
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
