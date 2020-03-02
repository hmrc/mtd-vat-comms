/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}

class NameModelSpec extends BaseSpec {

  val jsonModel: JsObject = Json.obj(
    "line1" -> "Dovah",
    "line2" -> "Kin",
    "line3" -> "Dragon Born"
  )

  val model: NameModel = NameModel("Dovah", Some("Kin"), Some("Dragon Born"))

  "nameModel" should {
    "correctly parse to Json" in {
      Json.toJson(model) shouldBe jsonModel
    }
    "correctly parse from Json" in {
      jsonModel.as[NameModel] shouldBe model
    }
  }
}
