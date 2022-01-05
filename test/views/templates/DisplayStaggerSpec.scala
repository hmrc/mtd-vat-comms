/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package views.templates

import views.ViewBaseSpec
import views.html.templates.DisplayStagger

class DisplayStaggerSpec extends ViewBaseSpec {

  val displayStagger: DisplayStagger = injector.instanceOf[DisplayStagger]

  "The Display Stagger template" should {

    "return the stagger message for an MA stagger code" in {
      val staggerText = displayStagger("MA").body.trim
      staggerText shouldBe "April, July, October and January."
    }

    "return the stagger message for an MB stagger code" in {
      val staggerText = displayStagger("MB").body.trim
      staggerText shouldBe "May, August, November and February."
    }

    "return the stagger message for an MC stagger code" in {
      val staggerText = displayStagger("MC").body.trim
      staggerText shouldBe "March, June, September and December."
    }

    "return the stagger message for an MM stagger code" in {
      val staggerText = displayStagger("MM").body.trim
      staggerText shouldBe "every month."
    }

    "throw an exception when the stagger code is unrecognised" in {
      intercept[MatchError](displayStagger("MZ"))
    }
  }
}
