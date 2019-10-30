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

package utils

import models._
import models.secureMessageAlertModels.messageTypes._
import play.api.libs.json.{JsValue, Json, OFormat}
import utils.LoggerUtil._

object SecureCommsMessageParser {
  private def convertToCamelCase(inputString: String): String = {
    val allLowerCase = inputString.toLowerCase
      .replace("-", " ")
      .split(" ")

    allLowerCase.head + allLowerCase.tail.map(word => word.head.toUpper + word.tail).mkString
  }

  def parseMessage(message: String): Either[ErrorModel, JsValue] = {
    try {
      val stringAsMap = message
        .stripPrefix("<![CDATA[").stripSuffix("]]>").replace("P>", "p>")
        .replace("<p>", "").split("</p>").filter(_.nonEmpty)
        .map { keyValuePair =>
          val splitValues = keyValuePair.split("\\|", 2)
          convertToCamelCase(splitValues.head) -> splitValues(1)
        }.toMap[String, String]

      Right(Json.toJson(stringAsMap))
    } catch {
      case _: Throwable =>
        logError("[SecureCommsMessageParser][parseMessage] Error performing generic parse")
        Left(JsonParsingError)
    }
  }

  def parseModel(model: SecureCommsMessageModel): Either[ErrorModel, MessageModel] = {
    model match {
      case x@SecureCommsMessageModel(_, _, _, _, Some(_), None, None, None, None, None, None, None, _, _, _) =>
        Right(toGivenModel[DeRegistrationModel](x))
      case x@SecureCommsMessageModel(_, _, _, _, None, Some(_), None, None, None, None, None, None, _, _, _) =>
        Right(toGivenModel[PPOBChangeModel](x))
      case x@SecureCommsMessageModel(_, _, _, _, None, None, Some(_), None, None, None, None, None, _, _, _) =>
        Right(toGivenModel[RepaymentsBankAccountChangeModel](x))
      case x@SecureCommsMessageModel(_, _, _, _, None, None, None, Some(_), None, None, None, None, _, _, _) =>
        Right(toGivenModel[VATStaggerChangeModel](x))
      case x@SecureCommsMessageModel(_, _, _, _, None, None, None, None, Some(_), None, None, None, _, _, _) =>
        Right(toGivenModel[EmailAddressChangeModel](x))
      case x@SecureCommsMessageModel(_, _, _, _, None, None, None, None, None, Some(_), None, None, _, _, _) =>
        Right(toGivenModel[OptOutModel](x))
      case x@SecureCommsMessageModel(_, _, _, _, None, None, None, None, None, None, Some(_), None, _, _, _) =>
        Right(toGivenModel[WebAddressChangeModel](x))
      case x@SecureCommsMessageModel(_, _, _, _, None, None, None, None, None, None, None, Some(_), _, _, _) =>
        Right(toGivenModel[ContactNumbersChangeModel](x))
      case x: SecureCommsMessageModel =>
        logError("[SecureCommsMessageParser][parseModel] Error parsing generic type into specific type\n" +
          "Populated optional fields:" + generateStringFromOptionalFields(x)
        )
        Left(SpecificParsingError)
    }
  }

  private def generateStringFromOptionalFields(input: SecureCommsMessageModel): String =
    input.effectiveDateOfDeregistration.fold("")(_ => "\n- Effective Date of Deregistration") +
    input.addressDetails.fold("")(_ => "\n- Address Details") +
    input.staggerDetails.fold("")(_ => "\n- Stagger") +
    input.originalEmailAddress.fold("")(_ => "\n- Original Email Address") +
    input.websiteAddress.fold("")(_ => "\n- Website Address") +
    input.contactNumbers.fold("")(_ => "\n- Contact Numbers")

  private def toGivenModel[T <: MessageModel](model: SecureCommsMessageModel)(implicit ev: OFormat[T]): T = Json.toJson(model).as[T]

}
