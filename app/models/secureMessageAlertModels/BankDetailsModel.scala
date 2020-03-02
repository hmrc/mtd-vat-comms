/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import play.api.libs.json.{Json, OFormat}

case class BankDetailsModel(
                             bankAccountName: String,
                             bankAccountNumber: String,
                             bankSortCode: String
                           )

object BankDetailsModel {
  implicit val formats: OFormat[BankDetailsModel] = Json.format[BankDetailsModel]
}
