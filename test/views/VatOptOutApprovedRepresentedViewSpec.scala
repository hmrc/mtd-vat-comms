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

class VatOptOutApprovedRepresentedViewSpec extends ViewBaseSpec {

  "The opt out approved message" when {

    "a agent entity has requested to opt out the client out of MTD" should {

      lazy val view = views.html.vatOptOutApprovedRepresented()
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct first paragraph" in {
        elementText("p:nth-child(1)") shouldBe "This does not cancel your VAT registration."
      }

      "have the correct second paragraph" in {
        elementText("p:nth-child(2)") shouldBe
          "For your current return period, your agent must continue to submit your VAT Returns using software compatible with Making Tax Digital."
      }

      "have the correct third paragraph" in {
        elementText("p:nth-child(3)") shouldBe
          "Future VAT Returns must be submitted using your online VAT account, " +
            "starting from your next return period. This change can take 2 days to come into effect."
      }

      "have the correct fourth paragraph" in {
        elementText("p:nth-child(4)") shouldBe
          "If your taxable turnover goes above £85,000, you or your agent must contact us to sign up again for Making Tax Digital."
      }
    }
  }
}
