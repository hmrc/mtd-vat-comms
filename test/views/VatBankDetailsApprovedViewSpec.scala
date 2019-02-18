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

class VatBankDetailsApprovedViewSpec extends ViewBaseSpec {

  "Rendering the VatBankDetailsApproved secure message content" should {

    lazy val view = views.html.vatBankDetailsApproved("Mickey Flanagan", "21****", "****3218")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct h2" in {
      elementText("h2") shouldBe "You have successfully changed your bank details for VAT repayments"
    }

    "have the correct first paragraph" in {
      elementText("p:nth-child(2)") shouldBe "Your new bank details for VAT repayments are:"
    }

    "have the correct user information" in {
      elementText("p:nth-child(3)") shouldBe
        "Account name: Mickey Flanagan " +
        "Sort code: 21**** " +
        "Account number: ****3218"
    }
  }
}
