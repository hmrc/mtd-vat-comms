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
import org.scalatest.{BeforeAndAfterEach, Inspectors, LoneElement}
import play.api.Configuration
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.ReadPreference
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.mongo.{MongoConnector, MongoSpecSupport}
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem.WorkItem
import models.VatChangePayload

import scala.concurrent.ExecutionContext.Implicits.global

class CommsEventQueueRepositorySpec extends UnitSpec
  with MongoSpecSupport
  with BeforeAndAfterEach
  with ScalaFutures
  with Inspectors
  with LoneElement
  with IntegrationPatience {

  val anInstant = DateTimeUtils.now

  def repoAtInstant(anInstant: DateTime): CommsEventQueueRepository =
    new CommsEventQueueRepository(Configuration(), new ReactiveMongoComponent {
      override def mongoConnector: MongoConnector = mongoConnectorForTest
    }) {
      override lazy val inProgressRetryAfter: Duration = Duration.standardHours(1)
      override lazy val retryIntervalMillis: Long      = 10000L
      override def now: DateTime                       = anInstant
    }

  lazy val repo = repoAtInstant(anInstant)

  override protected def beforeEach(): Unit = {
    await(repo.drop)
    await(repo.ensureIndexes)
  }

  "CommsEventQueue Repository" should {

    val vatChangePayload = VatChangePayload("Approved", "123", "PPOB Change")

    "ensure indexes are created" in {
      repo.collection.indexesManager.list().futureValue.size shouldBe 4
    }

    "be able to save and reload a vat change request" in {
      val workItem       = repo.pushNew(vatChangePayload, anInstant).futureValue

      repo.findById(workItem.id).futureValue.get should have(
        'item (vatChangePayload),
        'status (uk.gov.hmrc.workitem.ToDo),
        'receivedAt (anInstant),
        'updatedAt (anInstant)
      )
    }

    "be able to save the same requests twice" in {
      val payloadDetails = vatChangePayload
      repo.pushNew(vatChangePayload, anInstant).futureValue
      repo.pushNew(vatChangePayload, anInstant).futureValue

      val requests = repo.findAll(ReadPreference.primaryPreferred).futureValue
      requests should have(size(2))

      every(requests) should have(
        'item (payloadDetails),
        'status (uk.gov.hmrc.workitem.ToDo),
        'receivedAt (anInstant),
        'updatedAt (anInstant)
      )
    }

    "pull ToDo assess requests" in {
      val payloadDetails = vatChangePayload
      repo.pushNew(payloadDetails, anInstant).futureValue

      val repoLater: CommsEventQueueRepository = repoAtInstant(anInstant.plusMillis(1))

      repoLater.pullOutstanding.futureValue.get should have(
        'item (payloadDetails),
        'status (uk.gov.hmrc.workitem.InProgress)
      )
    }

    "pull nothing if no assess requests exist" in {
      repo.pullOutstanding.futureValue should be(None)
    }

    "not pull assess requests failed after the failedBefore time" in {
      val workItem: WorkItem[VatChangePayload] = repo.pushNew(vatChangePayload, anInstant).futureValue
      repo.markAs(workItem.id, uk.gov.hmrc.workitem.Failed).futureValue should be(true)

      repo.pullOutstanding.futureValue should be(None)
    }

    "complete and delete a assess requests if it is in progress" in {
      //given
      val workItem = repo.pushNew(vatChangePayload, anInstant).futureValue
      repo.markAs(workItem.id, uk.gov.hmrc.workitem.InProgress).futureValue should be(true)

      //when
      repo.complete(workItem.id).futureValue should be(true)

      //then
      repo.findById(workItem.id).futureValue shouldBe None
    }

    "not complete a assess requests if it is not in progress" in {
      //given
      val workItem = repo.pushNew(vatChangePayload, anInstant).futureValue

      //when
      repo.complete(workItem.id).futureValue should be(false)

      //then
      repo.findById(workItem.id).futureValue shouldBe Some(workItem)
    }

    "not complete a assess requests if it cannot be found" in {
      repo.complete(BSONObjectID.generate).futureValue should be(false)
    }
  }

}
