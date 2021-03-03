/*
 * Copyright 2021 HM Revenue & Customs
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
import views.html.VatEmailApproved

class VatEmailApprovedViewSpec extends ViewBaseSpec {

  val vatEmailApproved: VatEmailApproved = injector.instanceOf[VatEmailApproved]

  "Rendering the VatEmailApproved secure message content" should {

    lazy val view = vatEmailApproved("email@address.com")
    lazy implicit val document: Document = Jsoup.parse(view.body)

    "have the correct first paragraph" in {
      elementText("p:nth-child(1)") shouldBe "You want to be contacted by email."
    }
    "have the correct second paragraph" in {
      elementText("p:nth-child(2)") shouldBe "Your new email address for VAT is: email@address.com"
    }
  }
}
