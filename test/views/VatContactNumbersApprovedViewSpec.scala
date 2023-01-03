/*
 * Copyright 2023 HM Revenue & Customs
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
import views.html.VatContactNumbersApproved

class VatContactNumbersApprovedViewSpec extends ViewBaseSpec {

  val vatContactNumbersApproved: VatContactNumbersApproved = injector.instanceOf[VatContactNumbersApproved]

  "The contact numbers approved message" when {

    "a principal entity has requested to change their contact numbers" should {

      lazy val view = vatContactNumbersApproved()
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
