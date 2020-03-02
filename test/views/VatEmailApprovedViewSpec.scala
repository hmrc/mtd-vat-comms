/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatEmailApprovedViewSpec extends ViewBaseSpec {

  "Rendering the VatEmailApproved secure message content" should {

    lazy val view = views.html.vatEmailApproved("email@address.com")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct paragraph" in {
      elementText("p") shouldBe "Your new email address for VAT is: email@address.com"
    }
  }
}
