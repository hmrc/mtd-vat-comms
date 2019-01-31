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
import play.api.libs.json.Json

class VatChangeEventSpec extends BaseSpec {

  val eventModel = VatChangeEvent(
    "Approved",
    "1234567890",
    "Email Address Change"
  )

  "VatChangeEvent" should {

    "be constructed from recognised JSON" in {

      val inputJson = Json.obj(
        "status" -> "Approved",
        "BPContactNumber" -> "1234567890",
        "BPContactType" -> "Email Address Change"
      )

      inputJson.as[VatChangeEvent] shouldBe eventModel
    }

    "write to the correct JSON structure" in {

      val outputJson = Json.obj(
        "status" -> "Approved",
        "refNumber" -> "1234567890",
        "changeType" -> "Email Address Change"
      )

      Json.toJson(eventModel) shouldBe outputJson
    }
  }
}
