/*
 * Copyright 2022 HM Revenue & Customs
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
import views.html.VatDeregRejected

class VatDeregRejectedViewSpec extends ViewBaseSpec {

  val vatDeregRejected: VatDeregRejected = injector.instanceOf[VatDeregRejected]

  "Rendering the VatDeregRejected secure message content" should {

    lazy val view = vatDeregRejected()
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct first paragraph" in {
      elementText("p:nth-child(1)") shouldBe "From the information available to us, we think you are not eligible to " +
        "cancel your VAT registration and will remain registered."
    }

    "have the correct h2" in {
      elementText("h2") shouldBe "What happens next"
    }

    "have the correct second paragraph" in {
      elementText("p:nth-child(3)") shouldBe
        "You can apply to cancel your VAT registration again if your turnover falls below the threshold."
    }

    "have the correct third fifth paragraph" in {
      elementText("p:nth-child(4)") shouldBe
        "If you do not agree with our decision, you can ask for a review by an HMRC officer not previously involved " +
          "in the matter. If you want a review, you should write to us within 30 days of receiving this message " +
          "giving the reasons why you do not agree with our decision. Write to:"
    }

    "have the correct fourth paragraph" in {
      elementText("p:nth-child(5)") shouldBe
        "HMRC VAT Registration Service Crown House Birch Street WOLVERHAMPTON WV1 4JX"
    }

    "have the correct final paragraph" in {
      elementText("p:nth-child(6)") shouldBe "You also have the option to appeal to the tax tribunal " +
        "to request a review of an HMRC decision."
    }

    "have a link to appeal to the tax tribunal" in {
      elementAttributes("a")(document)("href") shouldBe
        "https://www.gov.uk/tax-tribunal/appeal-to-tribunal"
    }

  }
}

