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

import org.joda.time.{DateTime, Duration}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.BeforeAndAfterEach
import config.AppConfig
import mocks.MockAppConfig
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.ReadPreference
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.mongo.{MongoConnector, MongoSpecSupport}
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem.WorkItem
import models.VatChangeEvent
import play.api.Configuration

import scala.concurrent.ExecutionContext.Implicits.global

class CommsEventQueueRepositorySpec extends UnitSpec with MongoSpecSupport with BeforeAndAfterEach with ScalaFutures
  with IntegrationPatience {

  implicit val mockAppConfig: AppConfig = new MockAppConfig(Configuration())

  val anInstant: DateTime = DateTimeUtils.now

  val mongoConnector: MongoConnector = mongoConnectorForTest

  val reactiveMongoComponent: ReactiveMongoComponent = new ReactiveMongoComponent {
    override def mongoConnector: MongoConnector = mongoConnectorForTest
  }

  def repoAtInstant(anInstant: DateTime): CommsEventQueueRepository = new CommsEventQueueRepository(
    mockAppConfig, reactiveMongoComponent) {
      override lazy val inProgressRetryAfter: Duration = Duration.standardHours(1)
      override def now: DateTime                       = anInstant
  }

  lazy val repo: CommsEventQueueRepository = repoAtInstant(anInstant)

  override protected def beforeEach(): Unit = {
    await(repo.drop)
    await(repo.ensureIndexes)
  }

  "CommsEventQueue Repository" should {

    val vatChangeEvent: VatChangeEvent = VatChangeEvent("Approved", "123", "PPOB Change")

    "ensure indexes are created" in {
      repo.collection.indexesManager.list().futureValue.size shouldBe 4
    }

    "be able to save and reload a vat change request" in {
      val workItem = repo.pushNew(vatChangeEvent, anInstant).futureValue

      repo.findById(workItem.id).futureValue.get should have(
        'item (vatChangeEvent),
        'status (uk.gov.hmrc.workitem.ToDo),
        'receivedAt (anInstant),
        'updatedAt (anInstant)
      )
    }

    "be able to save the same requests twice" in {
      val payloadDetails = vatChangeEvent
      repo.pushNew(vatChangeEvent, anInstant).futureValue
      repo.pushNew(vatChangeEvent, anInstant).futureValue

      val requests = repo.findAll(ReadPreference.primaryPreferred).futureValue
      requests should have(size(2))

      every(requests) should have(
        'item (payloadDetails),
        'status (uk.gov.hmrc.workitem.ToDo),
        'receivedAt (anInstant),
        'updatedAt (anInstant)
      )
    }

    "pull ToDo vat change requests" in {
      val payloadDetails = vatChangeEvent
      repo.pushNew(payloadDetails, anInstant).futureValue

      val repoLater: CommsEventQueueRepository = repoAtInstant(anInstant.plusMillis(1))

      repoLater.pullOutstanding.futureValue.get should have(
        'item (payloadDetails),
        'status (uk.gov.hmrc.workitem.InProgress)
      )
    }

    "pull nothing if no vat change requests exist" in {
      repo.pullOutstanding.futureValue should be(None)
    }

    "not pull vat change requests failed after the failedBefore time" in {
      val workItem: WorkItem[VatChangeEvent] = repo.pushNew(vatChangeEvent, anInstant).futureValue
      repo.markAs(workItem.id, uk.gov.hmrc.workitem.Failed).futureValue should be(true)

      repo.pullOutstanding.futureValue should be(None)
    }

    "complete and delete a vat change request if it is in progress" in {
      val workItem = repo.pushNew(vatChangeEvent, anInstant).futureValue
      repo.markAs(workItem.id, uk.gov.hmrc.workitem.InProgress).futureValue should be(true)
      repo.complete(workItem.id).futureValue should be(true)

      repo.findById(workItem.id).futureValue shouldBe None
    }

    "not complete a vat change request if it is not in progress" in {

      val workItem = repo.pushNew(vatChangeEvent, anInstant).futureValue
      repo.complete(workItem.id).futureValue should be(false)
      repo.findById(workItem.id).futureValue shouldBe Some(workItem)
    }

    "not complete a vat change request if it cannot be found" in {
      repo.complete(BSONObjectID.generate).futureValue should be(false)
    }
  }
}
