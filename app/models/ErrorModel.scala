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

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class ErrorModel(code: String, body: String)

object ErrorModel {
  implicit val writes: Writes[ErrorModel] = (
    (__ \ "code").write[String] and
    (__ \ "body").write[String]
  )(unlift(ErrorModel.unapply))
}

object SpecificParsingError extends ErrorModel("ERROR_PARSING", "There has been an issue parsing the generic MessageModel into a specific type")
object GenericParsingError extends ErrorModel("ERROR_PARSING", "There has been an issue creating the generic model.")
object JsonParsingError extends ErrorModel("ERROR_PARSING", "Unable to parse SecureCommText string into Json.")

object UnableToParseSecureCommsResponseError extends ErrorModel("ERROR_PARSING", "Unable to parse the returned response from SecureComms.")
object UnableToParseSecureCommsErrorResponseError extends ErrorModel("ERROR_PARSING", "Unable to parse the returned error response from SecureComms.")
