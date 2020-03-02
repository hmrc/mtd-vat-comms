/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.templates.dates

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.templates.TemplateBaseSpec

class DisplayDateRangeTemplateSpec extends TemplateBaseSpec {

  "Calling displayDateRange template" when {

    val startDate = "20170101"

    "start and end dates are in the same year" should {

      val endDate = "20170401"

      lazy val template = views.html.templates.dates.displayDateRange(startDate, endDate)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the correct text" in {
        document.body().text() shouldEqual "1 January 2017 and 1 April 2017"
      }
    }

    "start and end dates are in the same year with short month format" should {

      val endDate = "20170401"

      lazy val template = views.html.templates.dates.displayDateRange(startDate, endDate, useShortMonthFormat = true)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the correct text" in {
        document.body().text() shouldEqual "1 Jan 2017 and 1 Apr 2017"
      }
    }

    "start and end dates are not in the same year" should {

      val endDate = "20180401"

      lazy val template = views.html.templates.dates.displayDateRange(startDate, endDate)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the correct text" in {
        document.body().text() shouldEqual "1 January 2017 and 1 April 2018"
      }
    }

    "start and end dates are not in the same year with short month format" should {

      val endDate = "20180401"

      lazy val template = views.html.templates.dates.displayDateRange(startDate, endDate, useShortMonthFormat = true)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the correct text" in {
        document.body().text() shouldEqual "1 Jan 2017 and 1 Apr 2018"
      }
    }
  }
}
