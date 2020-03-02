/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatBankDetailsApprovedViewSpec extends ViewBaseSpec {

  "Rendering the VatBankDetailsApproved secure message content" should {

    lazy val view = views.html.vatBankDetailsApproved("Mickey Flanagan", "21****", "****3218")
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
