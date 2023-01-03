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

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.VatBankDetailsApproved

class VatBankDetailsApprovedViewSpec extends ViewBaseSpec {

  val vatBankDetailsApproved: VatBankDetailsApproved = injector.instanceOf[VatBankDetailsApproved]

  "Rendering the VatBankDetailsApproved secure message content" should {

    lazy val view = vatBankDetailsApproved("Mickey Flanagan", "21****", "****3218")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct first paragraph" in {
      elementText("p:nth-child(1)") shouldBe "Your new bank details for VAT repayments are:"
    }

    "have the correct user information" in {
      elementText("p:nth-child(2)") shouldBe
        "Account name: Mickey Flanagan " +
          "Sort code: 21**** " +
          "Account number: ****3218"
    }
  }
}
