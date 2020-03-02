/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import play.api.libs.json.{Json, OFormat}

case class CustomerModel(
                          customerEmail: String,
                          customerEmailStatus: String
                        )

object CustomerModel {
  implicit val formats: OFormat[CustomerModel] = Json.format[CustomerModel]
}
