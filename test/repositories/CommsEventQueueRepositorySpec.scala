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
import org.joda.time.Duration
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.BeforeAndAfterEach
import reactivemongo.api.ReadPreference
import reactivemongo.bson.BSONObjectID
import models.VatChangeEvent
import uk.gov.hmrc.workitem._
import utils.MongoComponents

class CommsEventQueueRepositorySpec extends BaseSpec with MongoComponents with BeforeAndAfterEach with ScalaFutures
  with IntegrationPatience {

  override protected def beforeEach(): Unit = {
    await(repo.drop)
    await(repo.ensureIndexes)
  }

  "CommsEventQueue Repository" should {

    val vatChangeEvent: VatChangeEvent = VatChangeEvent("Approved", "123", "PPOB Change")

    "generate a Duration of 10000 milliseconds from config value" in {
      val dur: Duration = Duration.millis(mockAppConfig.retryIntervalMillis)
      dur.getMillis shouldBe 10000L
    }

    "ensure indexes are created" in {
      repo.collection.indexesManager.list().futureValue.size shouldBe 4
    }

    "be able to save and reload a vat change request" in {
      val workItem = repo.pushNew(vatChangeEvent, anInstant).futureValue

      repo.findById(workItem.id).futureValue.get should have(
        'item (vatChangeEvent),
        'status (ToDo),
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
        'status (ToDo),
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
        'status (InProgress)
      )
    }

    "pull nothing if no vat change requests exist" in {
      repo.pullOutstanding.futureValue should be(None)
    }

    "not pull vat change requests failed after the failedBefore time" in {
      val workItem: WorkItem[VatChangeEvent] = repo.pushNew(vatChangeEvent, anInstant).futureValue
      repo.markAs(workItem.id, Failed).futureValue should be(true)

      repo.pullOutstanding.futureValue should be(None)
    }

    "complete and delete a vat change request if it is in progress" in {
      val workItem = repo.pushNew(vatChangeEvent, anInstant).futureValue
      repo.markAs(workItem.id, InProgress).futureValue should be(true)
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
