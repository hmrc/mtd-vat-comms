/*
 * Copyright 2019 HM Revenue & Customs
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

package models

import base.BaseSpec
import play.api.libs.json.{JsObject, JsResultException, Json}

class VatChangeEventSpec extends BaseSpec {

  val eventModel = VatChangeEvent(
    "Approved",
    "1234567890",
    "Email Address Change"
  )

  val eventJson: JsObject = Json.obj(
    "status" -> "Approved",
    "BPContactNumber" -> "1234567890",
    "BPContactType" -> "Email Address Change"
  )

  "VatChangeEvent" should {

    "be constructed from recognised JSON" in {
      eventJson.as[VatChangeEvent] shouldBe eventModel
    }

    "fail to be constructed if a key is missing from the JSON (or named incorrectly)" in {

      val inputJson = Json.obj(
        "changeStatus" -> "Approved",
        "BPContactNumber" -> "1234567890",
        "BPContactType" -> "Email Address Change"
      )

      intercept[JsResultException](inputJson.as[VatChangeEvent])
    }

    "write to the correct JSON structure" in {
      Json.toJson(eventModel) shouldBe eventJson
    }
  }
}
