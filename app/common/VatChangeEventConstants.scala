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

package common

import play.api.libs.json.{JsObject, Json}
import play.api.mvc.AnyContentAsText

object VatChangeEventConstants {

  val invalidJsonRequest: JsObject = Json.obj(
    "name" -> "Pepsi Mac",
    "password" -> "pa55w0rd",
    "favouriteColour" -> "blue"
  )

  val invalidJsonResponse: JsObject = Json.obj(
    "code" -> "INVALID_JSON",
    "body" -> "Json received, but did not validate"
  )

  val invalidRequestBody = AnyContentAsText("status|Approved|BPContactNumber|1234567890|BPContactType|MyChange")

  val invalidFormatResponse: JsObject = Json.obj(
    "code" -> "INVALID_FORMAT",
    "body" -> s"Body of request was not JSON, ${invalidRequestBody.toString}"
  )
}
