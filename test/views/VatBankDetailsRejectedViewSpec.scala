/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatBankDetailsRejectedViewSpec extends ViewBaseSpec {

  "Rendering the VatBankDetailsRejected secure message content" should {

    lazy val view = views.html.vatBankDetailsRejected("Mickey Flanagan", "Acme Dynamite Solutions")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct first paragraph" in {
      elementText("p:nth-child(1)") shouldBe
        "This is because the bank account name is different from the business name on your VAT certificate."
    }

    "have the correct user information" in {
      elementText("p:nth-child(2)") shouldBe
        "Bank account name: Mickey Flanagan " +
          "Business name: Acme Dynamite Solutions"
    }

    "have the correct second paragraph" in {
      elementText("p:nth-child(3)") shouldBe
        "HMRC can only make payments to the bank account that belongs to the registered business."
    }

    "have the correct h2" in {
      elementText("h2") shouldBe "What happens next"
    }

    "have the correct third paragraph" in {
      elementText("div > div > p:nth-child(5)") shouldBe
        "If you do not agree with our decision, you can ask for a review by an HMRC officer not previously involved " +
          "in the matter. If you want a review, you should write to us within 30 days of receiving this message " +
          "giving the reasons why you do not agree with our decision. Write to:"
    }

    "have the correct HMRC address as the final paragraph" in {
      elementText("div > div > p:nth-child(6)") shouldBe
        "HMRC VAT Registration Service Crown House Birch Street WOLVERHAMPTON WV1 4JX"
    }
  }
}
