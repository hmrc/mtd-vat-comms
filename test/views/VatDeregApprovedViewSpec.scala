/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatDeregApprovedViewSpec extends ViewBaseSpec {

  "Rendering the VatDeregApproved secure message content" should {

    lazy val view = views.html.vatDeregApproved("03 October 2018")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct first paragraph" in {
      elementText("p:nth-child(1)") shouldBe "The business’ VAT registration will be cancelled on 03 October 2018."
    }

    "have the correct second h2" in {
      elementText("h2:nth-child(2)") shouldBe "What happens next"
    }

    "have the correct second paragraph" in {
      elementText("p:nth-child(3)") shouldBe "Submit any outstanding VAT Returns which cover " +
        "the period up to 03 October 2018."
    }

    "have the correct final paragraph" in {
      elementText("p:nth-child(4)") shouldBe "Register for VAT again if your taxable turnover " +
        "goes above the VAT threshold."
    }

  }
}
