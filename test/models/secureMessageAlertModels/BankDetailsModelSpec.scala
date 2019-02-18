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

package models.secureMessageAlertModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}

class BankDetailsModelSpec extends BaseSpec {
  val expectedModel: BankDetailsModel = BankDetailsModel(
    "Bank of Tamriel", "1029384756", "11-11-11"
  )

  val validJson: JsObject = Json.obj(
    "bankAccountName" -> "Bank of Tamriel",
    "bankAccountNumber" -> "1029384756",
    "bankSortCode" -> "11-11-11"
  )

  "Bank Details model" should {
    "parse from the correct json structure" in {
      validJson.as[BankDetailsModel] shouldBe expectedModel
    }
  }
}
