/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import play.api.libs.json.{Json, OFormat}

case class AddressDetailsModel(
                           addressLine1: String,
                           addressLine2: String,
                           addressLine3: String,
                           addressLine4: String,
                           addressLine5: String,
                           postCode: String,
                           countryName: String
                         )

object AddressDetailsModel {
  implicit val formats: OFormat[AddressDetailsModel] = Json.format[AddressDetailsModel]
}
