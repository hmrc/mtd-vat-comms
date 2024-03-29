/*
 * Copyright 2023 HM Revenue & Customs
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
import views.html.templates.dates.DisplayDateRange
import views.templates.TemplateBaseSpec

class DisplayDateRangeTemplateSpec extends TemplateBaseSpec {

  val displayDateRange: DisplayDateRange = injector.instanceOf[DisplayDateRange]

  "Calling displayDateRange template" when {

    val startDate = "20170101"

    "start and end dates are in the same year" should {

      val endDate = "20170401"

      lazy val template = displayDateRange(startDate, endDate)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the correct text" in {
        document.body().text() shouldEqual "1\u00a0January\u00a02017 and 1\u00a0April\u00a02017"
      }
    }

    "start and end dates are in the same year with short month format" should {

      val endDate = "20170401"

      lazy val template = displayDateRange(startDate, endDate, useShortMonthFormat = true)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the correct text" in {
        document.body().text() shouldEqual "1\u00a0Jan\u00a02017 and 1\u00a0Apr\u00a02017"
      }
    }

    "start and end dates are not in the same year" should {

      val endDate = "20180401"

      lazy val template = displayDateRange(startDate, endDate)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the correct text" in {
        document.body().text() shouldEqual "1\u00a0January\u00a02017 and 1\u00a0April\u00a02018"
      }
    }

    "start and end dates are not in the same year with short month format" should {

      val endDate = "20180401"

      lazy val template = displayDateRange(startDate, endDate, useShortMonthFormat = true)
      lazy val document: Document = Jsoup.parse(template.body)

      "render the correct text" in {
        document.body().text() shouldEqual "1\u00a0Jan\u00a02017 and 1\u00a0Apr\u00a02018"
      }
    }
  }
}
