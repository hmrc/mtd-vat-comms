/*
 * Copyright 2020 HM Revenue & Customs
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

import base.BaseSpec

class DateFormatterSpec extends BaseSpec {

  val validInput: String = "20181231"
  val invalidInput: String = "12312018"
  val expectedOutputValue: String = "31 December 2018"
  val expectedOutputValueForInvalidInput: String = ""

  "DateFormatter etmpToFullMonthDateString" should {
    "convert input in valid format to the correct output format" in {
      DateFormatter.etmpToFullMonthDateString(validInput) shouldBe expectedOutputValue
    }
  }

  "DateFormatter etmpToFullMonthDateString" should {
    "return an empty string in the case of invalid input value not in the expected format" in {

      DateFormatter.etmpToFullMonthDateString(invalidInput) shouldBe expectedOutputValueForInvalidInput
    }
  }
}
