/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}
import common.Constants.EmailStatus._

class CustomerModelSpec extends BaseSpec {
  val expectedModel: CustomerModel = CustomerModel(
    "aname@acompany.co.uk", VERIFIED
  )

  val validJson: JsObject = Json.obj(
    "customerEmail" -> "aname@acompany.co.uk",
    "customerEmailStatus" -> VERIFIED
  )

  val expectedModel2: CustomerModel = CustomerModel(
    "aname@acompany.co.uk", UNVERIFIED
  )

  val validJson2: JsObject = Json.obj(
    "customerEmail" -> "aname@acompany.co.uk",
    "customerEmailStatus" -> UNVERIFIED
  )

  "Customer model" should {
    s"parse from the correct json structure with $VERIFIED" in {
      validJson.as[CustomerModel] shouldBe expectedModel
    }

    s"parse from the correct json structure with $UNVERIFIED" in {
      validJson2.as[CustomerModel] shouldBe expectedModel2
    }
  }
}
