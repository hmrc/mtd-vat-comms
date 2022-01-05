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

package base

import akka.actor.ActorSystem
import modules.SchedulerModule
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.i18n.MessagesApi
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc._
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext

trait BaseSpec extends AnyWordSpecLike with Matchers with GuiceOneAppPerSuite {
  implicit val actorSystem: ActorSystem = ActorSystem("TestActorSystem")

  override implicit lazy val app: Application = new GuiceApplicationBuilder().disable[SchedulerModule].build
  lazy val injector: Injector = app.injector
  implicit lazy val ec: ExecutionContext = injector.instanceOf[ExecutionContext]
  implicit lazy val cc: ControllerComponents = injector.instanceOf[ControllerComponents]
  implicit lazy val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  implicit val hc: HeaderCarrier = HeaderCarrier()
  lazy val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
}
