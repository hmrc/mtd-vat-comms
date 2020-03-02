/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}

class TransactorModelSpec extends BaseSpec {
  val expectedModel: TransactorModel = TransactorModel(
    "dovah@whiterun.co.uk", "Dovah Kin"
  )

  val validJson: JsObject = Json.obj(
    "transactorEmail" -> "dovah@whiterun.co.uk",
    "transactorName" -> "Dovah Kin"
  )

  "Transactor model" should {
    "parse from the correct json structure" in {
      validJson.as[TransactorModel] shouldBe expectedModel
    }
  }
}
