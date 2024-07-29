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
import views.html.VatWebsiteApproved

class VatWebsiteApprovedViewSpec extends ViewBaseSpec {

  val vatWebsiteApproved: VatWebsiteApproved = injector.instanceOf[VatWebsiteApproved]

  val website = "http://www.website.com"

  "The website approved message" when {

    "a principal entity has requested to change their website" should {

      lazy val view = vatWebsiteApproved(isTransactor = false, Some(website))
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct paragraph" in {
        elementText("p") shouldBe s"Your new website address for VAT is: $website"
      }
    }

    "a principal entity has requested to remove their website" should {

      lazy val view = vatWebsiteApproved(isTransactor = false, None)
      lazy implicit val document: Document = Jsoup.parse(view.body)

      "have the correct paragraph" in {
        elementText("p") shouldBe "You can add a website address for VAT on your business details page."
      }
    }

    "an agent has requested to remove their client's website" should {

      lazy val view = vatWebsiteApproved(isTransactor = true, None)
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
