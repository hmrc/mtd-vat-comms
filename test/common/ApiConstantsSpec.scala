/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package common

import ApiConstants._
import base.BaseSpec
import models.VatChangeEvent
import play.api.libs.json.Json

class ApiConstantsSpec extends BaseSpec {

  "The vatChangeEventJson function" should {

    "produce the correct Json when provided with a change type" in {

        val expectedResult = Json.obj(
          "status" -> "Approved",
          "BPContactNumber" -> "123456789012",
          "BPContactType" -> "immaChangeType",
          "vrn" -> "123456789"
        )

        vatChangeEventJson("immaChangeType") shouldBe expectedResult
      }
  }

  "The vatChangeEventModel function" should {

    "provide the correct VatChangeEvent model when provided with a change type" in {

      val expectedResult = VatChangeEvent(
        "Approved",
        "123456789012",
        "immaChangeType",
        "123456789"
      )

      vatChangeEventModel("immaChangeType") shouldBe expectedResult
    }
  }
}
