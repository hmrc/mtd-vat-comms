/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import play.api.libs.json.{Json, OFormat}

case class TransactorModel(
                            transactorEmail: String,
                            transactorName: String
                          )

object TransactorModel {
  implicit val formats: OFormat[TransactorModel] = Json.format[TransactorModel]
}
