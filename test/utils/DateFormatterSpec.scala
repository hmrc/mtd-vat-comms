/*
 * Copyright 2020 HM Revenue & Customs
 *
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
