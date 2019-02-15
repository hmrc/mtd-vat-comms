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

class VatDeregRejectedViewSpec extends ViewBaseSpec {

  "Rendering the VatDeregRejected secure message content" should {

    lazy val view = views.html.vatDeregRejected("https://www.gov.uk/tax-tribunal/appeal-to-tribunal")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct h2" in {
      elementText("h2") shouldBe "We have rejected your request to deregister from VAT"
    }

    "have the correct first paragraph" in {
      elementText("p:nth-child(2)") shouldBe "From the information available to us, we think you are not eligible to " +
        "deregister from VAT and will remain registered."
    }

    "have the correct second h2" in {
      elementText("h2:nth-child(3)") shouldBe "What happens next"
    }

    "have the correct third paragraph" in {
      elementText("p:nth-child(4)") shouldBe "You can: Apply to deregister again if your turnover " +
        "falls below the threshold."
    }

    "have the correct fourth paragraph" in {
      elementText("p:nth-child(5)") shouldBe "If you do not agree with our decision, you can ask for a review by an " +
        "HMRC officer not previously involved in the matter. If you want a review, you should write to us within 30 " +
        "days of receiving this message giving the reasons why you do not agree with our decision. Write to:"
    }

    "have the correct final paragraph" in {
      elementText("p:nth-child(7)") shouldBe "You also have the option to appeal to the tax tribunal " +
        "(opens in a new tab) to request a review of an HMRC decision."
    }

    "have a link to appeal to the tax tribunal" in {
      elementAttributes("p:nth-child(7) > a")(document)("href") shouldBe
        "https://www.gov.uk/tax-tribunal/appeal-to-tribunal"
    }

  }
}
