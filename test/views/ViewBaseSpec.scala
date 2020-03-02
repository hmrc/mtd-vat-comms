/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import mocks.MockAppConfig
import org.jsoup.nodes.{Document, Element}
import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Configuration
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.inject.Injector
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.play.test.UnitSpec

import scala.collection.JavaConverters._

trait ViewBaseSpec extends UnitSpec with GuiceOneAppPerSuite with BeforeAndAfterAll {

  lazy implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
  lazy val injector: Injector = app.injector
  implicit val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]
  implicit val messages: Messages = Messages(Lang("en-GB"), messagesApi)
  implicit val mockAppConfig: MockAppConfig = new MockAppConfig(injector.instanceOf[Configuration])

  override def afterAll(): Unit = {
    app.stop()
    super.afterAll()
  }

  def element(cssSelector: String)(implicit document: Document): Element = {
    val elements = document.select(cssSelector)

    if(elements.size == 0) {
      fail(s"No element exists with the selector '$cssSelector'")
    }

    document.select(cssSelector).first()
  }

  def elementText(selector: String)(implicit document: Document): String = {
    element(selector).text()
  }

  def elementAttributes(cssSelector: String)(implicit document: Document): Map[String, String] = {
    val attributes = element(cssSelector).attributes.asList().asScala.toList
    attributes.map(attribute => (attribute.getKey, attribute.getValue)).toMap
  }
}
