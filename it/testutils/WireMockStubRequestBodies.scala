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

package testutils

import play.api.libs.json.Json

object WireMockStubRequestBodies {
  val validEDODString: String = "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
    "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p>" +
    "<p>EFFECTIVE DATE OF DEREGISTRATION|20181227</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
    "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p>" +
    "<p>CUSTOMER EMAIL STATUS|VERIFIED</p><p>NOTIFICATION PREFERENCE|EMAIL</p><p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p>" +
    "<p>FORMAT PREFERENCE|TEXT</p>"

  val secureCommsValidResponseEDOD: String => String = { processingDate =>
    val responseAsJsonObj = Json.obj(
      "processingDate" -> processingDate,
      "secureCommText" -> validEDODString
    )
    Json.prettyPrint(responseAsJsonObj)
  }

  val secureCommsInvalidResponseEDOD: String => String = { processingDate =>
    val responseAsJsonObj = Json.obj(
      "processingDate" -> processingDate
    )
    Json.prettyPrint(responseAsJsonObj)
  }

  val secureCommsValidErrorResponse: (String, String) => String = {
    case (code, reason) =>
      val responseAsJsonObj = Json.obj(
        "code" -> code,
        "reason" -> reason
      )
      Json.prettyPrint(responseAsJsonObj)
  }

  val secureCommsInvalidErrorResponse: String => String = {
    code =>
      val responseAsJsonObj = Json.obj(
        "code" -> code
      )
      Json.prettyPrint(responseAsJsonObj)
  }
}
