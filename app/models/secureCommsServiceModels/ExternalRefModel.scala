/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import play.api.libs.json.{Json, OFormat}
import common.Constants.SecureCommsServiceFieldValues.MDTP

case class ExternalRefModel(
                             id: String,
                             source: String = MDTP
                           )

object ExternalRefModel {
  implicit val formats: OFormat[ExternalRefModel] = Json.format[ExternalRefModel]
}
