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

package repositories

import mocks.MockAppConfig
import modules.SchedulerModule
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.Application
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.workitem.WorkItemRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.reflect.ClassTag

abstract class MongoSpec[A, T <: WorkItemRepository[A, _]](implicit classTag: ClassTag[T]) extends
  AnyWordSpecLike with Matchers with BeforeAndAfterEach with BeforeAndAfterAll {

  implicit lazy val app: Application = new GuiceApplicationBuilder().disable[SchedulerModule].build
  val injector: Injector = app.injector
  implicit val mockAppConfig: MockAppConfig = new MockAppConfig(app.configuration)

  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = injector.instanceOf[ExecutionContext]

  val repo: T = app.injector.instanceOf[T]

  def dropDatabase(): Unit = Await.result(repo.removeAll(), Duration.Inf)

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropDatabase()
  }

  override def afterAll(): Unit = {
    app.stop()
    super.afterAll()
  }
}
