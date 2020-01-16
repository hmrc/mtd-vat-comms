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

package models

import common.ApiConstants._
import base.BaseSpec
import play.api.libs.json.{JsResultException, Json}

class VatChangeEventSpec extends BaseSpec {

  val changeType = "Email Address Change"

  "VatChangeEvent" should {

    "be constructed from recognised JSON" in {
      vatChangeEventJson(changeType).as[VatChangeEvent] shouldBe vatChangeEventModel(changeType)
    }

    "fail to be constructed if the JSON is not in the correct format" in {

      val inputJson = Json.obj(
        "changeStatus" -> "Approved",
        "BPContactNumber" -> "1234567890",
        "BPContactType" -> "Email Address Change",
        "identifier" -> "123456789"
      )

      intercept[JsResultException](inputJson.as[VatChangeEvent])
    }

    "write to the correct JSON structure" in {
      Json.toJson(vatChangeEventModel(changeType)) shouldBe vatChangeEventJson(changeType)
    }
  }
}
