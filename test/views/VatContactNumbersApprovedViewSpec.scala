/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatContactNumbersApprovedViewSpec extends ViewBaseSpec {

  "The contact numbers approved message" when {

    "a principal entity has requested to change their contact numbers" should {

      lazy val view = views.html.vatContactNumbersApproved()
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have a link to the ChoC overview page" which {

        "has the correct text" in {
          elementText("a") shouldBe "View your updated contact details"
        }

        "has the correct link location" in {
          element("a").attr("href") shouldBe mockAppConfig.manageVatSubscriptionUrl
        }
      }
    }
  }
}
