/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.templates

import mocks.MockAppConfig
import org.jsoup.Jsoup
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Configuration
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.inject.Injector
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.play.test.UnitSpec

class TemplateBaseSpec extends UnitSpec with GuiceOneAppPerSuite {

  lazy implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
  lazy val injector: Injector = app.injector
  implicit val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]
  implicit val messages: Messages = Messages(Lang("en-GB"), messagesApi)

  def formatHtml(body: Html): String = Jsoup.parseBodyFragment(s"\n$body\n").toString.trim

}