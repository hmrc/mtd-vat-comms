/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatStaggerApprovedViewSpec extends ViewBaseSpec {

  "Rendering the VatStaggerApproved view for an 'MM' stagger code" should {

    lazy val view = views.html.vatStaggerApproved("MM")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct new VAT return dates" in {
      elementText("p:nth-child(1)") shouldBe "Your new return dates for VAT are every month."
    }

    "have the correct final paragraph" in {
      elementText("p:nth-child(2)") shouldBe "Your new return dates might only take effect from " +
        "your next tax period. Check what returns are currently due to make sure you do not miss any."
    }
  }

  "Rendering the VatStaggerApproved view for an 'MA' stagger code" should {

    lazy val view = views.html.vatStaggerApproved("MA")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct new VAT return dates" in {
      elementText("p:nth-child(1)") shouldBe "Your new return dates for VAT are April, July, October and January."
    }

    "have the correct final paragraph" in {
      elementText("p:nth-child(2)") shouldBe "Your new return dates might only take effect from " +
        "your next tax period. Check what returns are currently due to make sure you do not miss any."
    }
  }

  "Rendering the VatStaggerApproved view for an 'MB' stagger code" should {

    lazy val view = views.html.vatStaggerApproved("MB")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct new VAT return dates" in {
      elementText("p:nth-child(1)") shouldBe "Your new return dates for VAT are May, August, November and February."
    }

    "have the correct final paragraph" in {
      elementText("p:nth-child(2)") shouldBe "Your new return dates might only take effect from " +
        "your next tax period. Check what returns are currently due to make sure you do not miss any."
    }
  }

  "Rendering the VatStaggerApproved view for an 'MC' stagger code" should {

    lazy val view = views.html.vatStaggerApproved("MC")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct new VAT return dates" in {
      elementText("p:nth-child(1)") shouldBe "Your new return dates for VAT are March, June, September and December."
    }

    "have the correct final paragraph" in {
      elementText("p:nth-child(2)") shouldBe "Your new return dates might only take effect from " +
        "your next tax period. Check what returns are currently due to make sure you do not miss any."
    }
  }
}
