/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import models.viewModels.VatPPOBViewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatPPOBApprovedViewSpec extends ViewBaseSpec {

  "Rendering the VatPPOBApproved secure message content" should {

    val viewModel = VatPPOBViewModel(
      "21 Blackjack Street",
      "Stirchley",
      Some("Telford"),
      Some("Shropshire"),
      None,
      Some("TF2 5TH"),
      Some("United Kingdom")
    )

    lazy val view = views.html.vatPPOBApproved(viewModel)
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct first paragraph" in {
      elementText("p:nth-child(1)") shouldBe "Your new principal place of business for VAT is:"
    }

    "have the correct address" in {
      elementText("p:nth-child(2)") shouldBe "21 Blackjack Street Stirchley Telford Shropshire TF2 5TH United Kingdom"
    }
  }
}
