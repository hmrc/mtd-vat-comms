/*
 * Copyright 2024 HM Revenue & Customs
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

package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.VatStaggerApproved

class VatStaggerApprovedViewSpec extends ViewBaseSpec {

  val vatStaggerApproved: VatStaggerApproved = injector.instanceOf[VatStaggerApproved]

  "Rendering the VatStaggerApproved view for an 'MM' stagger code" should {

    lazy val view = vatStaggerApproved("MM")
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

    lazy val view = vatStaggerApproved("MA")
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

    lazy val view = vatStaggerApproved("MB")
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

    lazy val view = vatStaggerApproved("MC")
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
