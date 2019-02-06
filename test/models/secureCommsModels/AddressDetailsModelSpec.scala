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

package models.secureCommsModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}

class AddressDetailsModelSpec extends BaseSpec {

  val expectedModel: AddressDetailsModel = AddressDetailsModel(
    "Address Line Een",
    "Address Line Twee",
    "Probably a county",
    "",
    "Not sure why 5",
    "TF11TT",
    "England M8"
  )

  val validJson: JsObject = Json.obj(
    "addressLine1" -> "Address Line Een",
    "addressLine2" -> "Address Line Twee",
    "addressLine3" -> "Probably a county",
    "addressLine4" -> "",
    "addressLine5" -> "Not sure why 5",
    "postCode" -> "TF11TT",
    "countryName" -> "England M8"
  )

  "AddressDetails model" should {
    "parse from the correct json structure" in {
      validJson.as[AddressDetailsModel] shouldBe expectedModel
    }
  }
}
