/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.emailRendererModels

import play.api.libs.json.{Json, OFormat}

case class EmailRequestModel(
                                      to: Seq[String],
                                      templateId: String,
                                      parameters: Map[String, String],
                                      force: Boolean = false)

object EmailRequestModel {
  implicit val formats: OFormat[EmailRequestModel] = Json.format[EmailRequestModel]
}
