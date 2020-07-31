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

package common

import base.BaseSpec
import common.ApiConstants._
import models.VatChangeEvent
import play.api.libs.json.Json

class ApiConstantsSpec extends BaseSpec {

  "The vatChangeEventJson function" should {

    "produce the correct Json when provided with a change type" in {

        val expectedResult = Json.obj(
          "status" -> "Approved",
          "BPContactNumber" -> "123456789012",
          "BPContactType" -> "immaChangeType",
          "vrn" -> "123456789"
        )

        vatChangeEventJson("immaChangeType") shouldBe expectedResult
      }
  }

  "The vatChangeEventModel function" should {

    "provide the correct VatChangeEvent model when provided with a change type" in {

      val expectedResult = VatChangeEvent(
        "Approved",
        "123456789012",
        "immaChangeType",
        "123456789"
      )

      vatChangeEventModel("immaChangeType") shouldBe expectedResult
    }
  }
}
