/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatPPOBRejectedViewSpec extends ViewBaseSpec {

  "Rendering the VatPPOBRejected secure message content" should {

    lazy val view = views.html.vatPPOBRejected()
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct first paragraph" in {
      elementText("div.grid-row > div > p:nth-child(1)") shouldBe
        "Your principal place of business for VAT cannot be any of these address types:"
    }

    "have the correct first bullet point" in {
      elementText("div.grid-row > div > ul > li:nth-child(1)") shouldBe
        "the address of a third-party accountant or tax agent"
    }

    "have the correct second bullet point" in {
      elementText("div.grid-row > div > ul > li:nth-child(2)") shouldBe "a PO box address"
    }

    "have the correct third bullet point" in {
      elementText("div.grid-row > div > ul > li:nth-child(3)") shouldBe "a ‘care of’ address"
    }

    "have the correct h2" in {
      elementText("h2") shouldBe "What happens next"
    }

    "have a link to change business details in manage vat subscription frontend" in {
      elementAttributes("a")(document)("href") shouldBe
        "/vat-through-software/account/change-business-details"
    }

    "have the correct fourth paragraph" in {
      elementText("div.grid-row > div > p:nth-child(4)") shouldBe
        "Change your principal place of business to the address where the business does most of its work. " +
          "If this is in different locations, use the address where it keeps its business records."
    }

    "have the correct fifth paragraph" in {
      elementText("div.grid-row > div > p:nth-child(5)") shouldBe
        "If you do not agree with our decision, you can ask for a review by an HMRC officer not previously involved " +
          "in the matter. If you want a review, you should write to us within 30 days of receiving this message " +
          "giving the reasons why you do not agree with our decision. Write to:"
    }

    "have the correct HMRC address as the final paragraph" in {
      elementText("div.grid-row > div > p:nth-child(6)") shouldBe
        "HMRC VAT Registration Service Crown House Birch Street WOLVERHAMPTON WV1 4JX"
    }
  }
}
