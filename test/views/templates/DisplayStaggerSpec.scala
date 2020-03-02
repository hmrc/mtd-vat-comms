/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.templates

import views.ViewBaseSpec

class DisplayStaggerSpec extends ViewBaseSpec {

  "The Display Stagger template" should {

    "return the stagger message for an MA stagger code" in {
      val staggerText = views.html.templates.displayStagger("MA").body.trim
      staggerText shouldBe "April, July, October and January."
    }

    "return the stagger message for an MB stagger code" in {
      val staggerText = views.html.templates.displayStagger("MB").body.trim
      staggerText shouldBe "May, August, November and February."
    }

    "return the stagger message for an MC stagger code" in {
      val staggerText = views.html.templates.displayStagger("MC").body.trim
      staggerText shouldBe "March, June, September and December."
    }

    "return the stagger message for an MM stagger code" in {
      val staggerText = views.html.templates.displayStagger("MM").body.trim
      staggerText shouldBe "every month."
    }

    "throw an exception when the stagger code is unrecognised" in {
      intercept[MatchError](views.html.templates.displayStagger("MZ"))
    }
  }
}
