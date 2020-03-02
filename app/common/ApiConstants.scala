/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package common

import models.VatChangeEvent
import play.api.libs.json.{JsObject, Json}

object ApiConstants {

  def vatChangeEventJson(changeType: String): JsObject = Json.obj(
    "status" -> "Approved",
    "BPContactNumber" -> "123456789012",
    "BPContactType" -> changeType,
    "vrn" -> "123456789"
  )

  def vatChangeEventModel(changeType: String): VatChangeEvent = VatChangeEvent(
    "Approved",
    "123456789012",
    changeType,
    "123456789"
  )

  val serviceName = "value-added-tax"
}
