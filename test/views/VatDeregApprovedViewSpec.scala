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

class VatDeregApprovedViewSpec extends ViewBaseSpec {

  "Rendering the VatDeregApproved secure message content for a client" should {

    lazy val view = views.html.vatDeregApproved("03 October 2018", isTransactor = false)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct h2" in {
      elementText("h2") shouldBe "We have accepted your request to deregister from VAT"
    }

    "have the correct first paragraph" in {
      elementText("p:nth-child(2)") shouldBe "The business will be deregistered from VAT on 03 October 2018."
    }

    "have the correct second h2" in {
      elementText("h2:nth-child(3)") shouldBe "What happens next"
    }

    "have the correct second paragraph" in {
      elementText("p:nth-child(4)") shouldBe "Submit any outstanding VAT Returns which cover " +
        "the period up to 03 October 2018."
    }

    "have the correct final paragraph" in {
      elementText("p:nth-child(5)") shouldBe "Register for VAT again if your taxable turnover " +
        "goes above the VAT threshold."
    }

  }

  "Rendering the VatDeregApproved secure message content for a transactor" should {

    lazy val view = views.html.vatDeregApproved("03 October 2018", isTransactor = true)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct h2" in {
      elementText("h2") shouldBe "We have accepted your agent’s request to deregister from VAT"
    }

    "have the correct first paragraph" in {
      elementText("p:nth-child(2)") shouldBe "The business will be deregistered from VAT on 03 October 2018."
    }

    "have the correct second h2" in {
      elementText("h2:nth-child(3)") shouldBe "What happens next"
    }

    "have the correct second paragraph" in {
      elementText("p:nth-child(4)") shouldBe "Submit any outstanding VAT Returns which cover " +
        "the period up to 03 October 2018."
    }

    "have the correct final paragraph" in {
      elementText("p:nth-child(5)") shouldBe "Register for VAT again if your taxable turnover " +
        "goes above the VAT threshold."
    }

  }
}
