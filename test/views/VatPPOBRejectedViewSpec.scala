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

class VatPPOBRejectedViewSpec extends ViewBaseSpec {

  "Rendering the VatPPOBRejected secure message content" should {

    lazy val view = views.html.vatPPOBRejected("http://localhost:9150/vat-through-software/account/change-business-details")
    lazy implicit val document: Document = Jsoup.parse(view.body)


    "have the correct h2" in {
      elementText("h2") shouldBe "We have rejected the change to your principal place of business for VAT"
    }

    "have the correct first paragraph" in {
      elementText("div.grid-row > div > p:nth-child(2)") shouldBe "Your principal place of business for VAT cannot be any of these address types:"
    }

    "have the correct first bullet point" in {
      elementText("div.grid-row > div > ul > li:nth-child(1)") shouldBe "the address of a third-party accountant or tax agent"
    }

    "have the correct second bullet point" in {
      elementText("div.grid-row > div > ul > li:nth-child(2)") shouldBe "a PO box address"
    }

    "have the correct third bullet point" in {
      elementText("div.grid-row > div > ul > li:nth-child(3)") shouldBe "a ‘care of’ address"
    }

    "have the correct second h2" in {
      elementText("div.grid-row > div > h2:nth-child(4)") shouldBe "What happens next"
    }

    "have a link to change business details in manage vat subscription frontend" in {
      elementAttributes("div.grid-row > div > p:nth-child(5) > a")(document)("href") shouldBe
        "http://localhost:9150/vat-through-software/account/change-business-details"
    }

    "have the correct fourth paragraph" in {
      elementText("div.grid-row > div > p:nth-child(5)") shouldBe "Change your principal place of business to the " +
        "address where the business does most of its work. If this is in different locations, use the address " +
        "where it keeps its business records."
    }

    "have the correct fifth paragraph" in {
      elementText("div.grid-row > div > p:nth-child(6)") shouldBe "If you do not agree with our decision, you can ask for a review by an " +
        "HMRC officer not previously involved in the matter. If you want a review, you should write to us within 30 " +
        "days of receiving this message giving the reasons why you do not agree with our decision. Write to:"
    }

    "have the correct HMRC address as the final paragraph" in {
      elementText("div.grid-row > div > p:nth-child(7)") shouldBe "HMRC VAT Registration Service Crown House Birch " +
        "Street WOLVERHAMPTON WV1 4JX"
    }

  }
}
