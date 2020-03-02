/*
 * Copyright 2020 HM Revenue & Customs
 *
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
    "body" -> s"Request body was not JSON."
  )
}
