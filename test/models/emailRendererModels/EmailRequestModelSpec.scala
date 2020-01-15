/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
