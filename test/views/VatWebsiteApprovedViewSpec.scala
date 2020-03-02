/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatWebsiteApprovedViewSpec extends ViewBaseSpec {

  val website = "http://www.website.com"

  "The website approved message" when {

    "a principal entity has requested to change their website" should {

      lazy val view = views.html.vatWebsiteApproved(isTransactor = false, Some(website))
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct paragraph" in {
        elementText("p") shouldBe s"Your new website address for VAT is: $website"
      }
    }

    "a principal entity has requested to remove their website" should {

      lazy val view = views.html.vatWebsiteApproved(isTransactor = false, None)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct paragraph" in {
        elementText("p") shouldBe "You can add a website address for VAT on your business details page."
      }
    }

    "an agent has requested to remove their client's website" should {

      lazy val view = views.html.vatWebsiteApproved(isTransactor = true, None)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct paragraph" in {
        elementText("p") shouldBe "You or your agent can add a website address for VAT on your business details page."
      }

      "have a link to the ChoC overview page" which {

        "has the correct text" in {
          elementText("a") shouldBe "add a website address for VAT"
        }

        "has the correct link location" in {
          element("a").attr("href") shouldBe mockAppConfig.manageVatSubscriptionUrl
        }
      }
    }
  }
}
