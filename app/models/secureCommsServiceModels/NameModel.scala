/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import play.api.libs.json.{Json, OFormat}

case class NameModel(
                      line1: String,
                      line2: Option[String] = None,
                      line3: Option[String] = None
                    )

object NameModel {
  implicit val formats: OFormat[NameModel] = Json.format[NameModel]
}
