/*
 * Copyright 2021 HM Revenue & Customs
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
import views.html.VatStaggerRejected

class VatStaggerRejectedViewSpec extends ViewBaseSpec {

  val vatStaggerRejected: VatStaggerRejected = injector.instanceOf[VatStaggerRejected]

  "Rendering the VatStaggerRejected secure message content" should {

    lazy val view = vatStaggerRejected()
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct h2" in {
      elementText("h2") shouldBe "What happens next"
    }

    "have the correct first paragraph" in {
      elementText("div > div > p:nth-child(2)") shouldBe "You must continue to submit to your current deadlines."
    }

    "have the correct second paragraph" in {
      elementText("div > div > p:nth-child(3)") shouldBe
        "If you do not agree with our decision, you can ask for a review by an HMRC officer not previously involved " +
          "in the matter. If you want a review, you should write to us within 30 days of receiving this message " +
          "giving the reasons why you do not agree with our decision. Write to:"
    }

    "have the correct HMRC address as the final paragraph" in {
      elementText("div > div > p:nth-child(4)") shouldBe
        "HMRC VAT Registration Service Crown House Birch Street WOLVERHAMPTON WV1 4JX"
    }
  }
}
