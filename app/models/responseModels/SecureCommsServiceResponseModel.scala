package models.responseModels

import play.api.libs.json.{Json, Reads}

case class SecureCommsServiceResponseModel(id: String)

object SecureCommsServiceResponseModel {
  implicit val reads: Reads[SecureCommsServiceResponseModel] = Json.reads[SecureCommsServiceResponseModel]
}
