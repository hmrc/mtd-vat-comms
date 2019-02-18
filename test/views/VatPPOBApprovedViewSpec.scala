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

import models.viewModels.VatPPOBViewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatPPOBApprovedViewSpec extends ViewBaseSpec {

  "Rendering the VatPPOBApproved secure message content" should {

    val viewModel = VatPPOBViewModel("21 Blackjack Street", "Stirchley", Some("Telford"), Some("Shropshire"), None, Some("TF2 5TH"), Some("United Kingdom"))

    lazy val view = views.html.vatPPOBApproved(viewModel)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct h2" in {
      elementText("h2") shouldBe "You have successfully changed your principal place of business for VAT"
    }

    "have the correct first paragraph" in {
      elementText("p:nth-child(2)") shouldBe "Your new principal place of business for VAT is:"
    }

    "have the correct address" in {
      elementText("p:nth-child(3)") shouldBe "21 Blackjack Street Stirchley Telford Shropshire TF2 5TH United Kingdom"
    }

  }
}
