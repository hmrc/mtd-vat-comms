/*
 * Copyright 2024 HM Revenue & Customs
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
import common.Constants.EmailStatus._
import play.api.libs.json.{JsObject, Json}

class CustomerModelSpec extends BaseSpec {
  val expectedModel: CustomerModel = CustomerModel(
    "aname@acompany.co.uk", VERIFIED
  )

  val validJson: JsObject = Json.obj(
    "customerEmail" -> "aname@acompany.co.uk",
    "customerEmailStatus" -> VERIFIED
  )

  val expectedModel2: CustomerModel = CustomerModel(
    "aname@acompany.co.uk", UNVERIFIED
  )

  val validJson2: JsObject = Json.obj(
    "customerEmail" -> "aname@acompany.co.uk",
    "customerEmailStatus" -> UNVERIFIED
  )

  "Customer model" should {
    s"parse from the correct json structure with $VERIFIED" in {
      validJson.as[CustomerModel] shouldBe expectedModel
    }

    s"parse from the correct json structure with $UNVERIFIED" in {
      validJson2.as[CustomerModel] shouldBe expectedModel2
    }
  }
}
