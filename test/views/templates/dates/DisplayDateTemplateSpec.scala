/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.templates.dates

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.templates.TemplateBaseSpec

class DisplayDateTemplateSpec extends TemplateBaseSpec {

  "Calling displayDate" when {

    val date = "20170101"

    "showYear is true" should {

      lazy val template = views.html.templates.dates.displayDate(date)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the date with year" in {
        document.body().text() shouldEqual "1 January 2017"
      }
    }

    "showYear is true and use short month format is true" should {

      lazy val template = views.html.templates.dates.displayDate(date, useShortMonthFormat = true)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the date with year" in {
        document.body().text() shouldEqual "1 Jan 2017"
      }
    }

    "showYear is false" should {

      lazy val template = views.html.templates.dates.displayDate(date, showYear = false)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the date without year" in {
        document.body().text() shouldEqual "1 January"
      }
    }

    "showYear is false and use short month format is true" should {

      lazy val template = views.html.templates.dates.displayDate(date, showYear = false, useShortMonthFormat = true)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the date without year" in {
        document.body().text() shouldEqual "1 Jan"
      }
    }
  }
}