/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}

class RecipientModelSpec extends BaseSpec {

  val jsonModel: JsObject = Json.obj(
    "taxIdentifier" -> Json.obj(
      "name" -> "key",
      "value" -> "value"
    ),
    "name" -> Json.obj(
      "line1" -> "Lydia"
    ),
    "email" -> "swornToCarryYourBurdens@whiterun.tam"
  )

  val model: RecipientModel = RecipientModel(
    TaxIdentifierModel("key", "value"),
    NameModel("Lydia"),
    "swornToCarryYourBurdens@whiterun.tam"
  )

  "nameModel" should {
    "correctly parse to Json" in {
      Json.toJson(model) shouldBe jsonModel
    }
    "correctly parse from Json" in {
      jsonModel.as[RecipientModel] shouldBe model
    }
  }
}
