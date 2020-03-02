/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.emailRendererModels

import base.BaseSpec
import play.api.libs.json.Json

class EmailRequestModelSpec extends BaseSpec{

  val emailToUse = Seq("thisAnEmailYo@sup.sweet.dude")
  val templateId = "thisBeAnIdMaMan"
  val params = Map(
    "num1" -> "aight",
    "num2" -> "ello"
  )

  "EmailRequestModel" should {
    "correctly render as Json" in {
      val validJson1 = Json.toJson(Json.obj(
        "to" -> emailToUse,
        "templateId" -> templateId,
        "parameters" -> params,
        "force" -> false
      ))
      val validJson2 = Json.toJson(Json.obj(
        "to" -> emailToUse,
        "templateId" -> templateId,
        "parameters" -> params,
        "force" -> true
      ))

      val result1 = Json.toJson(EmailRequestModel(
        emailToUse, templateId, params
      ))
      val result2 = Json.toJson(EmailRequestModel(
        emailToUse, templateId, params, force = true
      ))

      result1 shouldBe validJson1
      result2 shouldBe validJson2
    }

    "correctly read from Json" in {
      val validJson1 = Json.toJson(Json.obj(
        "to" -> emailToUse,
        "templateId" -> templateId,
        "parameters" -> params,
        "force" -> false
      ))
      val validJson2 = Json.toJson(Json.obj(
        "to" -> emailToUse,
        "templateId" -> templateId,
        "parameters" -> params,
        "force" -> true
      ))

      val result1 = validJson1.validate[EmailRequestModel]
      val result2 = validJson2.validate[EmailRequestModel]

      result1.get shouldBe EmailRequestModel(
        emailToUse, templateId, params
      )
      result2.get shouldBe EmailRequestModel(
        emailToUse, templateId, params, force = true
      )
    }
  }
}
