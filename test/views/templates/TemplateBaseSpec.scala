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

package views.templates

import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.inject.Injector
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.twirl.api.Html

class TemplateBaseSpec extends AnyWordSpecLike with Matchers with GuiceOneAppPerSuite {

  lazy implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
  lazy val injector: Injector = app.injector
  implicit val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]
  implicit val messages: Messages = MessagesImpl(Lang("en-GB"), messagesApi)

  def formatHtml(body: Html): String = Jsoup.parseBodyFragment(s"\n$body\n").toString.trim

}