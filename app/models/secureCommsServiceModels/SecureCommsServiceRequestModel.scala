/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureCommsServiceModels

import play.api.libs.json.{Json, OFormat}

case class SecureCommsServiceRequestModel(
                                           externalRef: ExternalRefModel,
                                           recipient: RecipientModel,
                                           messageType: String,
                                           subject: String,
                                           content: String
                                         )

object SecureCommsServiceRequestModel {
  implicit val formats: OFormat[SecureCommsServiceRequestModel] = Json.format[SecureCommsServiceRequestModel]
}
