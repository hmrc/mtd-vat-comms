/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}

class BankDetailsModelSpec extends BaseSpec {
  val expectedModel: BankDetailsModel = BankDetailsModel(
    "Bank of Tamriel", "1029384756", "11-11-11"
  )

  val validJson: JsObject = Json.obj(
    "bankAccountName" -> "Bank of Tamriel",
    "bankAccountNumber" -> "1029384756",
    "bankSortCode" -> "11-11-11"
  )

  "Bank Details model" should {
    "parse from the correct json structure" in {
      validJson.as[BankDetailsModel] shouldBe expectedModel
    }
  }
}
