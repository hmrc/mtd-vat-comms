/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}

class TaxIdentifierModelSpec extends BaseSpec {

  val jsonModel: JsObject = Json.obj(
    "name" -> "key",
    "value" -> "value"
  )

  val model: TaxIdentifierModel = TaxIdentifierModel("key", "value")

  "nameModel" should {
    "correctly parse to Json" in {
      Json.toJson(model) shouldBe jsonModel
    }
    "correctly parse from Json" in {
      jsonModel.as[TaxIdentifierModel] shouldBe model
    }
  }
}
