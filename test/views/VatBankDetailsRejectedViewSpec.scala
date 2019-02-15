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

import models.viewModels.VatBankDetailsViewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatBankDetailsRejectedViewSpec extends ViewBaseSpec {

  "Rendering the VatBankDetailsRejected secure message content" should {

    val viewModel = VatBankDetailsViewModel("Mickey Flanagan", None, None, Some("Acme Dynamite Solutions"))

    lazy val view = views.html.vatBankDetailsRejected(viewModel)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct h2" in {
      elementText("h2") shouldBe "We have rejected the change to your bank details for VAT repayments"
    }

    "have the correct first paragraph" in {
      elementText("p:nth-child(2)") shouldBe "This is because the bank account name is different from the business name on your VAT certificate."
    }

    "have the correct account name" in {
      elementText("p:nth-child(3)") shouldBe "Bank account name: Mickey Flanagan"
    }

    "have the correct business name" in {
      elementText("p:nth-child(4)") shouldBe "Business name: Acme Dynamite Solutions"
    }

    "have the correct second paragraph" in {
      elementText("p:nth-child(5)") shouldBe "HMRC can only make payments to the bank account that belongs to the registered business."
    }

    "have the correct second h2" in {
      elementText("h2:nth-child(6)") shouldBe "What happens next"
    }

    "have the correct third paragraph" in {
      elementText("div > div > p:nth-child(7)") shouldBe "If you do not agree with our decision, you can ask for a review by an " +
        "HMRC officer not previously involved in the matter. If you want a review, you should write to us within 30 " +
        "days of receiving this message giving the reasons why you do not agree with our decision. Write to:"
    }

    "have the correct HMRC address as the final paragraph" in {
      elementText("div > div > p:nth-child(8)") shouldBe "HMRC VAT Registration Service Crown House Birch " +
        "Street WOLVERHAMPTON WV1 4JX"

    }
  }
}
