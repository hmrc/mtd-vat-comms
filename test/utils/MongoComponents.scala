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

package utils

import mocks.MockAppConfig
import org.joda.time.{DateTime, Duration}
import play.modules.reactivemongo.ReactiveMongoComponent
import repositories.CommsEventQueueRepository
import uk.gov.hmrc.mongo.{MongoConnector, MongoSpecSupport}
import uk.gov.hmrc.time.DateTimeUtils

trait MongoComponents extends MongoSpecSupport{

  val mockAppConfig: MockAppConfig

  val anInstant: DateTime = DateTimeUtils.now

  val reactiveMongoComponent: ReactiveMongoComponent = new ReactiveMongoComponent {
    override def mongoConnector: MongoConnector = mongoConnectorForTest
  }

  def repoAtInstant(anInstant: DateTime): CommsEventQueueRepository = new CommsEventQueueRepository(
    mockAppConfig, reactiveMongoComponent) {
    override lazy val inProgressRetryAfter: Duration = Duration.standardHours(1)

    override def now: DateTime = anInstant
  }

  lazy val repo: CommsEventQueueRepository = repoAtInstant(anInstant)

}
