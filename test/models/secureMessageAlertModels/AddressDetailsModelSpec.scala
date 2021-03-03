/*
 * Copyright 2021 HM Revenue & Customs
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

package models.secureMessageAlertModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}

class AddressDetailsModelSpec extends BaseSpec {

  val expectedModel: AddressDetailsModel = AddressDetailsModel(
    "Address Line One",
    "Address Line Two",
    "County",
    "",
    "",
    "AB12CD",
    "England"
  )

  val validJson: JsObject = Json.obj(
    "addressLine1" -> "Address Line One",
    "addressLine2" -> "Address Line Two",
    "addressLine3" -> "County",
    "addressLine4" -> "",
    "addressLine5" -> "",
    "postCode" -> "AB12CD",
    "countryName" -> "England"
  )

  "AddressDetails model" should {
    "parse from the correct json structure" in {
      validJson.as[AddressDetailsModel] shouldBe expectedModel
    }
  }
}
