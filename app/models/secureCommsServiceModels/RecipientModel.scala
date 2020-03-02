/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import play.api.libs.json.{Json, OFormat}

case class RecipientModel(
                           taxIdentifier: TaxIdentifierModel,
                           name: NameModel,
                           email: String
                         )

object RecipientModel {
  implicit val formats: OFormat[RecipientModel] = Json.format[RecipientModel]
}
