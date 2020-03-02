/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}
import common.Constants.SecureCommsServiceFieldValues._

class SecureCommsServiceRequestModelSpec extends BaseSpec {

  val jsonModel: JsObject = Json.obj(
    "externalRef" -> Json.obj(
      "id" -> "fusRohID",
      "source" -> MDTP
    ),
    "recipient" -> Json.obj(
      "taxIdentifier" -> Json.obj(
        "name" -> "key",
        "value" -> "value"
      ),
      "name" -> Json.obj(
        "line1" -> "Lydia"
      ),
      "email" -> "swornToCarryYourBurdens@whiterun.tam"
    ),
    "messageType" -> "some message type",
    "subject" -> "WE'RE TELLING YOU SOMETHING",
    "content" -> "HERE HAVE SOME CONTENT"
  )

  val model: SecureCommsServiceRequestModel = SecureCommsServiceRequestModel(
    ExternalRefModel(
      "fusRohID",
      MDTP
    ),
    RecipientModel(
      TaxIdentifierModel("key", "value"),
      NameModel("Lydia"),
      "swornToCarryYourBurdens@whiterun.tam"
    ),
    "some message type",
    "WE'RE TELLING YOU SOMETHING",
    "HERE HAVE SOME CONTENT"
  )

  "nameModel" should {
    "correctly parse to Json" in {
      Json.toJson(model) shouldBe jsonModel
    }
    "correctly parse from Json" in {
      jsonModel.as[SecureCommsServiceRequestModel] shouldBe model
    }
  }
}
