/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatStaggerRejectedViewSpec extends ViewBaseSpec {

  "Rendering the VatStaggerRejected secure message content" should {

    lazy val view = views.html.vatStaggerRejected()
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct h2" in {
      elementText("h2") shouldBe "What happens next"
    }

    "have the correct first paragraph" in {
      elementText("div > div > p:nth-child(2)") shouldBe "You must continue to submit to your current deadlines."
    }

    "have the correct second paragraph" in {
      elementText("div > div > p:nth-child(3)") shouldBe
        "If you do not agree with our decision, you can ask for a review by an HMRC officer not previously involved " +
          "in the matter. If you want a review, you should write to us within 30 days of receiving this message " +
          "giving the reasons why you do not agree with our decision. Write to:"
    }

    "have the correct HMRC address as the final paragraph" in {
      elementText("div > div > p:nth-child(4)") shouldBe
        "HMRC VAT Registration Service Crown House Birch Street WOLVERHAMPTON WV1 4JX"
    }
  }
}
