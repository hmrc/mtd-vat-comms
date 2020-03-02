/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import play.api.libs.json.{Json, OFormat}

case class TaxIdentifierModel(
                               name: String,
                               value: String
                             )

object TaxIdentifierModel {
  implicit val formats: OFormat[TaxIdentifierModel] = Json.format[TaxIdentifierModel]
}
