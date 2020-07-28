/*
 * Copyright 2020 HM Revenue & Customs
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
import views.html.VatContactNumbersRejected

class VatContactNumbersRejectedViewSpec extends ViewBaseSpec {

  val vatContactNumbersRejected: VatContactNumbersRejected = injector.instanceOf[VatContactNumbersRejected]

  "The contact number rejected message" when {

    "the user has attempted to change their landline or mobile number" should {

      lazy val view = vatContactNumbersRejected()
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct first paragraph" in {
        elementText("p:nth-of-type(1)") shouldBe "The request to change your contact details for VAT has been rejected."
      }

      "have the correct h2" in {
        elementText("h2") shouldBe "What happens next"
      }

      "have the correct second paragraph" in {
        elementText("p:nth-of-type(2)") shouldBe
          "If you do not agree with our decision, you can ask for a review by an HMRC officer not previously involved " +
            "in the matter. If you want a review, you should write to us within 30 days of receiving this message " +
            "giving the reasons why you do not agree with our decision. Write to:"
      }

      "have the correct HMRC address as the final paragraph" in {
        elementText("p:nth-of-type(3)") shouldBe
          "HMRC VAT Registration Service Crown House Birch Street WOLVERHAMPTON WV1 4JX"
      }
    }
  }
}
