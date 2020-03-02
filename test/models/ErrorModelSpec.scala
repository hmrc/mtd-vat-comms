/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import base.BaseSpec
import play.api.libs.json.Json

class ErrorModelSpec extends BaseSpec {

  val errorModel = ErrorModel(
    "ERROR_1",
    "There has been an error."
  )

  "ErrorModel" should {

    "write to the correct JSON structure" in {

      val outputJson = Json.obj(
        "code" -> "ERROR_1",
        "body" -> "There has been an error."
      )

      Json.toJson(errorModel) shouldBe outputJson
    }
  }
}
