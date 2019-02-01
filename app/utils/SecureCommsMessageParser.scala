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

import play.api.libs.json.{JsValue, Json}

object SecureCommsMessageParser {
  private def convertToCamelCase(inputString: String): String = {
    val allLowerCase = inputString.toLowerCase
      .replace("-", " ")
      .split(" ")

    allLowerCase.head + allLowerCase.tail.map(word => word.head.toUpper + word.tail).mkString
  }

  def parseMessage(message: String): JsValue = {
    val stringAsMap = message.replace("<p>", "").split("</p>").filter(_.nonEmpty)
      .map { keyValuePair =>
        val splitValues = keyValuePair.split("\\|", 2)
        convertToCamelCase(splitValues.head) -> splitValues(1)
      }.toMap[String, String]

    Json.toJson(stringAsMap)
  }
}
