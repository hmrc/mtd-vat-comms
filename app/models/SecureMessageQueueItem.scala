/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import play.api.libs.json.{Json, OFormat}

case class SecureMessageQueueItem(status: String,
                                  secureCommsMessageModel: SecureCommsMessageModel)
object SecureMessageQueueItem {
  implicit val formats: OFormat[SecureMessageQueueItem] = Json.format[SecureMessageQueueItem]
}

