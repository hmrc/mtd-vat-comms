/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import common.ApiConstants._
import base.BaseSpec
import play.api.libs.json.{JsResultException, Json}

class VatChangeEventSpec extends BaseSpec {

  val changeType = "Email Address Change"

  "VatChangeEvent" should {

    "be constructed from recognised JSON" in {
      vatChangeEventJson(changeType).as[VatChangeEvent] shouldBe vatChangeEventModel(changeType)
    }

    "fail to be constructed if the JSON is not in the correct format" in {

      val inputJson = Json.obj(
        "changeStatus" -> "Approved",
        "BPContactNumber" -> "1234567890",
        "BPContactType" -> "Email Address Change",
        "identifier" -> "123456789"
      )

      intercept[JsResultException](inputJson.as[VatChangeEvent])
    }

    "write to the correct JSON structure" in {
      Json.toJson(vatChangeEventModel(changeType)) shouldBe vatChangeEventJson(changeType)
    }
  }
}
