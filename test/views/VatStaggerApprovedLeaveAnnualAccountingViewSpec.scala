/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VatStaggerApprovedLeaveAnnualAccountingViewSpec extends ViewBaseSpec {

  "Rendering the VatStaggerApprovedLeaveAnnualAccounting view for an 'MA' stagger code" should {

    lazy val view = views.html.vatStaggerApprovedLeaveAnnualAccounting("MA", "20190102","20190202","20180101")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct leaving annual accounting message " in {
      elementText("p:nth-child(1)") shouldBe "By changing your VAT Return dates, you have left the Annual Accounting scheme."
    }

    "have the correct dates for annual accounting" in {
      elementText("p:nth-child(2)") shouldBe "The dates for your Annual Accounting period are 2 January 2019 and 2 February 2019."
    }

    "have the correct info after the dates for annual accounting" in {
      elementText("p:nth-child(3)") shouldBe "An additional period will be added to cover the time between the end of your Annual " +
        "Accounting period and the start of your new tax period. The dates for additional tax period are 1 January 2018 and 2 January 2019."
    }

    "have the correct what happens next text" in {
      elementText("h2") shouldBe "What happens next"
    }

    "have the correct your new return dates" in {
      elementText("p:nth-child(5)") shouldBe "Your new return dates for VAT are: April, July, October and January."
    }

    "have the correct check what returns are currently due" in {
      elementText("p:nth-child(6)") shouldBe "Your new return dates will take effect from 2 January 2019. Check what returns are currently " +
        "due to make sure you do not miss any."
    }
  }


}
