/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package repositories

import akka.stream.Materializer
import mocks.MockAppConfig
import modules.SchedulerModule
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import play.api.Application
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.workitem.WorkItemRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.reflect.ClassTag

abstract class MongoSpec[A, T <: WorkItemRepository[A, _]](implicit classTag: ClassTag[T]) extends UnitSpec with BeforeAndAfterEach with BeforeAndAfterAll {

  implicit lazy val app: Application = new GuiceApplicationBuilder().disable[SchedulerModule].build
  val injector: Injector = app.injector
  implicit val mockAppConfig: MockAppConfig = new MockAppConfig(app.configuration)

  implicit val materializer: Materializer = app.materializer

  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext

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
