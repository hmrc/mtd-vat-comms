/*
 * Copyright 2024 HM Revenue & Customs
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

package views.templates.dates

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.templates.dates.DisplayDate
import views.templates.TemplateBaseSpec

class DisplayDateTemplateSpec extends TemplateBaseSpec {

  val displayDate: DisplayDate = injector.instanceOf[DisplayDate]

  "Calling displayDate" when {

    val date = "20170101"

    "showYear is true" should {

      lazy val template = displayDate(date)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the date with year" in {
        document.body().text() shouldEqual "1 January 2017"
      }

      "uses non breaking spaces to display the date" in {
        document.body().text().contains("1\u00a0January\u00a02017")
      }
    }

    "showYear is true and use short month format is true" should {

      lazy val template = displayDate(date, useShortMonthFormat = true)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the date with year" in {
        document.body().text() shouldEqual "1 Jan 2017"
      }
    }

    "showYear is false" should {

      lazy val template = displayDate(date, showYear = false)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the date without year" in {
        document.body().text() shouldEqual "1 January"
      }

      "uses non breaking spaces to display the year" in {
        document.body().text().contains("1\u00a0January")
      }
    }

    "showYear is false and use short month format is true" should {

      lazy val template = displayDate(date, showYear = false, useShortMonthFormat = true)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the date without year" in {
        document.body().text() shouldEqual "1 Jan"
      }
    }
  }
}