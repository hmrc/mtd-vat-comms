/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import play.api.libs.json.{Json, OFormat}

case class PreferencesModel(
                             notificationPreference: String,
                             channelPreference: String,
                             languagePreference: String,
                             formatPreference: String
                           )

object PreferencesModel {
  implicit val formats: OFormat[PreferencesModel] = Json.format[PreferencesModel]
}
