/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import play.api.libs.json.{Json, OFormat}

case class ContactNumbersModel(
                           primaryPhoneNumber: String,
                           primaryPhoneNumberChanged: String,
                           mobileNumber: String,
                           mobileNumberChanged: String
                         )

object ContactNumbersModel {
  implicit val formats: OFormat[ContactNumbersModel] = Json.format[ContactNumbersModel]
}
