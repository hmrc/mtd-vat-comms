/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import play.api.libs.json._

case class VatChangeEvent(status: String,
                          BPContactNumber: String,
                          BPContactType: String,
                          vrn: String)

object VatChangeEvent {
  implicit val formats: OFormat[VatChangeEvent] = Json.format[VatChangeEvent]
}
