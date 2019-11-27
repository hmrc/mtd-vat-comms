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

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatOptOutApprovedViewSpec extends ViewBaseSpec {

  "The opt out approved message" when {

    "a principal entity has requested to opt out of MTD" should {

      lazy val view = views.html.vatOptOutApproved("999999999")
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct first paragraph" in {
        elementText("p:nth-child(1)") shouldBe "This does not cancel your VAT registration."
      }

      "have the correct second paragraph" in {
        elementText("p:nth-child(2)") shouldBe "For your current return period, you must continue to submit your " +
          "VAT Returns using software compatible with Making Tax Digital."
      }

      "have the correct third paragraph" in {
        elementText("p:nth-child(3)") shouldBe "Future VAT Returns must be submitted using your online VAT account, " +
          "starting from your next return period."
      }

      "have a VAT account link" which {

        "has the correct text" in {
          elementText("p:nth-child(3) > a") shouldBe "your online VAT account"
        }

        "has the correct href" in {
          element("p:nth-child(3) > a").attr("href") shouldBe mockAppConfig.vatSummaryUrl
        }
      }

      "have the correct fourth paragraph" in {
        elementText("p:nth-child(4)") shouldBe
          "If your taxable turnover goes above £85,000, you must sign up again for Making Tax Digital."
      }

      "have a re-sign-up link" which {

        "has the correct text" in {
          elementText("p:nth-child(4) > a") shouldBe "sign up again"
        }

        "has the correct href" in {
          element("p:nth-child(4) > a").attr("href") shouldBe mockAppConfig.mtdSignUpUrl("999999999")
        }
      }
    }
  }
}

