/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class VatChangeEvent(status: String,
                          refNumber: String,
                          changeType: String)

object VatChangeEvent {

  implicit val reads: Reads[VatChangeEvent] = (
    (__ \ "status").read[String] and
    (__ \ "BPContactNumber").read[String] and
    (__ \ "BPContactType").read[String]
  )(VatChangeEvent.apply _)

  implicit val writes: Writes[VatChangeEvent] = (
    (__ \ "status").write[String] and
    (__ \ "refNumber").write[String] and
    (__ \ "changeType").write[String]
  )(unlift(VatChangeEvent.unapply))
}
