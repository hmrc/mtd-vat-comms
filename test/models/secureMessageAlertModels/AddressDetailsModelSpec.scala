/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}

class AddressDetailsModelSpec extends BaseSpec {

  val expectedModel: AddressDetailsModel = AddressDetailsModel(
    "Address Line Een",
    "Address Line Twee",
    "Probably a county",
    "",
    "Not sure why 5",
    "TF11TT",
    "England M8"
  )

  val validJson: JsObject = Json.obj(
    "addressLine1" -> "Address Line Een",
    "addressLine2" -> "Address Line Twee",
    "addressLine3" -> "Probably a county",
    "addressLine4" -> "",
    "addressLine5" -> "Not sure why 5",
    "postCode" -> "TF11TT",
    "countryName" -> "England M8"
  )

  "AddressDetails model" should {
    "parse from the correct json structure" in {
      validJson.as[AddressDetailsModel] shouldBe expectedModel
    }
  }
}
