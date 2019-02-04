/*
 * Copyright 2019 HM Revenue & Customs
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

package modules

import org.scalamock.scalatest.MockFactory
import play.api.{Configuration, Environment}
import services.CommsEventQueuePollingService
import uk.gov.hmrc.play.test.UnitSpec

class SchedulerModuleSpec extends UnitSpec with MockFactory {

  val configuration: Configuration = mock[Configuration]
  val environment: Environment = mock[Environment]

  "Schedule Module" should {

    "bind the QueuePollingService eagerly" in {
      val bindings = new SchedulerModule().bindings(environment, configuration)

      bindings.filter(p => p.key.clazz == classOf[CommsEventQueuePollingService]).head.eager shouldBe true
    }
  }
}
