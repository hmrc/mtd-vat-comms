/*
 * Copyright 2020 HM Revenue & Customs
 *
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
