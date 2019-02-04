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

package testOnly.controllers

import base.BaseSpec
import org.scalamock.scalatest.MockFactory
import play.api.mvc.Result
import play.modules.reactivemongo.ReactiveMongoComponent
import repositories.CommsEventQueueRepository
import uk.gov.hmrc.mongo.{MongoConnector, MongoSpecSupport}

import scala.concurrent.Future

class CommsEventQueueControllerSpec extends BaseSpec with MockFactory with MongoSpecSupport {

  val mongoComponent: ReactiveMongoComponent = new ReactiveMongoComponent {
    override def mongoConnector: MongoConnector = mongoConnectorForTest
  }
  lazy val repo = new CommsEventQueueRepository(mockAppConfig, mongoComponent)
  lazy val controller = new CommsEventQueueController(repo)

  "The count action" should {

    "return the total number of records in the CommsEventQueue database" in {
      await(repo.drop)
      await(repo.ensureIndexes)
      lazy val result: Future[Result] = controller.count(request)

      await(bodyOf(result)) shouldBe "0"
    }
  }
}
