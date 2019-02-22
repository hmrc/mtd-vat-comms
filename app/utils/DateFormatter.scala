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

import java.text.SimpleDateFormat

import scala.util.{Failure, Success, Try}

object DateFormatter {

  def etmpToFullMonthDateString(date: String): String = {
    Try {

      val inFormat = new SimpleDateFormat("yyyyMMdd")
      val outFormat = new SimpleDateFormat("dd MMMM yyyy")
      outFormat.setLenient(false)
      inFormat.setLenient(false)
      val formattedDate = outFormat.format(inFormat.parse(date))
      formattedDate

    } match {
      case Success(result) => result
      // just return an empty string in implementation
      case Failure(_) => ""
    }

  }

}
