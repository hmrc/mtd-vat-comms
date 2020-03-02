/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package base

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.ExecutionContext

trait BaseSpec extends UnitSpec {
  implicit val actorSystem: ActorSystem = ActorSystem("TestActorSystem")
  implicit val mat: ActorMaterializer = ActorMaterializer()

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  implicit val hc: HeaderCarrier = HeaderCarrier()
  val request = FakeRequest()
}
