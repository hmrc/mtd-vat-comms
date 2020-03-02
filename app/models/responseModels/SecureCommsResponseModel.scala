/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.responseModels

import play.api.libs.json.{Json, Reads}

case class SecureCommsResponseModel(processingDate: String, secureCommText: String)

object SecureCommsResponseModel {
  implicit val reads: Reads[SecureCommsResponseModel] = Json.reads[SecureCommsResponseModel]
}
