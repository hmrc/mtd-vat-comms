/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}
import common.Constants.SecureCommsServiceFieldValues.MDTP

class ExternalRefModelSpec extends BaseSpec {

  val jsonModel: JsObject = Json.obj(
    "id" -> "an id of some kind",
    "source" -> MDTP
  )

  val model: ExternalRefModel = ExternalRefModel("an id of some kind", MDTP)

  "nameModel" should {
    "correctly parse to Json" in {
      Json.toJson(model) shouldBe jsonModel
    }
    "correctly parse from Json" in {
      jsonModel.as[ExternalRefModel] shouldBe model
    }
  }
}
