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

package repositories

import base.BaseSpec
import org.joda.time.{DateTime, Duration}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.BeforeAndAfterEach
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.ReadPreference
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.mongo.{MongoConnector, MongoSpecSupport}
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem._
import models.Placeholder

class SecureMessageQueueRepositorySpec extends BaseSpec with MongoSpecSupport with BeforeAndAfterEach with ScalaFutures
  with IntegrationPatience {

  val anInstant: DateTime = DateTimeUtils.now

  val reactiveMongoComponent: ReactiveMongoComponent = new ReactiveMongoComponent {
    override def mongoConnector: MongoConnector = mongoConnectorForTest
  }

  def repoAtInstant(anInstant: DateTime): SecureMessageQueueRepository = new SecureMessageQueueRepository(
    mockAppConfig, reactiveMongoComponent) {
    override lazy val inProgressRetryAfter: Duration = Duration.standardHours(1)
    override def now: DateTime                       = anInstant
  }

  lazy val repo: SecureMessageQueueRepository = repoAtInstant(anInstant)

  override protected def beforeEach(): Unit = {
    await(repo.drop)
    await(repo.ensureIndexes)
  }

  "SecureMessageQueue Repository" should {

    val placeholder: Placeholder = Placeholder("name")

    "ensure indexes are created" in {
      repo.collection.indexesManager.list().futureValue.size shouldBe 4
    }

    "be able to save and reload an item" in {
      val workItem = repo.pushNew(placeholder, anInstant).futureValue

      repo.findById(workItem.id).futureValue.get should have(
        'item (placeholder),
        'status (ToDo),
        'receivedAt (anInstant),
        'updatedAt (anInstant)
      )
    }

    "be able to save the same item twice" in {
      val payloadDetails = placeholder
      repo.pushNew(placeholder, anInstant).futureValue
      repo.pushNew(placeholder, anInstant).futureValue

      val requests = repo.findAll(ReadPreference.primaryPreferred).futureValue
      requests should have(size(2))

      every(requests) should have(
        'item (payloadDetails),
        'status (ToDo),
        'receivedAt (anInstant),
        'updatedAt (anInstant)
      )
    }

    "pull ToDo items" in {
      val payloadDetails = placeholder
      repo.pushNew(payloadDetails, anInstant).futureValue

      val repoLater: SecureMessageQueueRepository = repoAtInstant(anInstant.plusMillis(1))

      repoLater.pullOutstanding.futureValue.get should have(
        'item (payloadDetails),
        'status (InProgress)
      )
    }

    "pull nothing if no items exist" in {
      repo.pullOutstanding.futureValue should be(None)
    }

    "not pull items failed after the failedBefore time" in {
      val workItem: WorkItem[Placeholder] = repo.pushNew(placeholder, anInstant).futureValue
      repo.markAs(workItem.id, Failed).futureValue should be(true)

      repo.pullOutstanding.futureValue should be(None)
    }

    "complete and delete an item if it is in progress" in {
      val workItem = repo.pushNew(placeholder, anInstant).futureValue
      repo.markAs(workItem.id, InProgress).futureValue should be(true)
      repo.complete(workItem.id).futureValue should be(true)

      repo.findById(workItem.id).futureValue shouldBe None
    }

    "not complete an item if it is not in progress" in {

      val workItem = repo.pushNew(placeholder, anInstant).futureValue
      repo.complete(workItem.id).futureValue should be(false)
      repo.findById(workItem.id).futureValue shouldBe Some(workItem)
    }

    "not complete an item if it cannot be found" in {
      repo.complete(BSONObjectID.generate).futureValue should be(false)
    }
  }
}
