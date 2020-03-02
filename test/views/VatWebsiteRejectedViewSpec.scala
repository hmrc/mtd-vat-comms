/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatWebsiteRejectedViewSpec extends ViewBaseSpec {

  "The website rejected message" when {

    "the user has attempted to change their website" should {

      lazy val view = views.html.vatWebsiteRejected(isRemoval = false)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct first paragraph" in {
        elementText("p:nth-of-type(1)") shouldBe "We have rejected the change to your website address for VAT."
      }

      "have the correct h2" in {
        elementText("h2") shouldBe "What happens next"
      }

      "have the correct second paragraph" in {
        elementText("p:nth-of-type(2)") shouldBe
          "If you do not agree with our decision, you can ask for a review by an HMRC officer not previously involved " +
            "in the matter. If you want a review, you should write to us within 30 days of receiving this message " +
            "giving the reasons why you do not agree with our decision. Write to:"
      }

      "have the correct HMRC address as the final paragraph" in {
        elementText("p:nth-of-type(3)") shouldBe
          "HMRC VAT Registration Service Crown House Birch Street WOLVERHAMPTON WV1 4JX"
      }
    }

    "the user has attempted to remove their website" should {

      lazy val view = views.html.vatWebsiteRejected(isRemoval = true)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct first paragraph" in {
        elementText("p:nth-of-type(1)") shouldBe "We have rejected the removal of your website address for VAT."
      }
    }
  }
}
