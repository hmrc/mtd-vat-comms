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

import common.ApiConstants.vatChangeEventModel
import models.VatChangeEvent
import org.joda.time.{DateTime, Duration}
import org.scalatest.BeforeAndAfterEach
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import reactivemongo.api.ReadPreference
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem._

class CommsEventQueueRepositorySpec extends MongoSpec[VatChangeEvent, CommsEventQueueRepository] with BeforeAndAfterEach {

  val anInstant: DateTime = DateTimeUtils.now

  "CommsEventQueue Repository" should {

    "ensure indexes are created" in {
      val indexList = await(repo.collection.indexesManager.list())
      indexList.size shouldBe 5
    }

    val vatChangeEvent: VatChangeEvent = vatChangeEventModel("PPOB Change")

    "generate a Duration of 10000 milliseconds from config value" in {
      val dur: Duration = Duration.millis(mockAppConfig.retryIntervalMillis)
      dur.getMillis shouldBe 10000L
    }

    "be able to save and reload a vat change request" in {
      val workItem = await(repo.pushNew(vatChangeEvent, anInstant))

      await(repo.findById(workItem.id)).get should have(
        'item (vatChangeEvent),
        'status (ToDo),
        'receivedAt (anInstant),
        'updatedAt (workItem.updatedAt)
      )
    }

    "be able to save the same requests twice" in {
      val payloadDetails = vatChangeEvent
      val firstItem = await(repo.pushNew(vatChangeEvent, anInstant))
      val secondItem = await(repo.pushNew(vatChangeEvent, anInstant))

      val requests = await(repo.findAll(ReadPreference.primaryPreferred))
      requests should have(size(2))


      requests.head should have(
        'item (payloadDetails),
        'status (ToDo),
        'receivedAt (anInstant),
        'updatedAt (firstItem.updatedAt)
      )
      requests(1) should have(
        'item (payloadDetails),
        'status (ToDo),
        'receivedAt (anInstant),
        'updatedAt (secondItem.updatedAt)
      )
    }

    "pull ToDo vat change requests" in {
      val payloadDetails = vatChangeEvent
      await(repo.pushNew(payloadDetails, anInstant))

      await(repo.pullOutstanding).get should have(
        'item (payloadDetails),
        'status (InProgress)
      )
    }

    "pull nothing if no vat change requests exist" in {
      await(repo.pullOutstanding) should be(None)
    }

    "not pull vat change requests failed after the failedBefore time" in {
      val workItem: WorkItem[VatChangeEvent] = await(repo.pushNew(vatChangeEvent, anInstant))
      await(repo.markAs(workItem.id, Failed)) shouldBe true

      await(repo.pullOutstanding) shouldBe None
    }

    "complete and delete a vat change request if it is in progress" in {
      val workItem = await(repo.pushNew(vatChangeEvent, anInstant))
      await(repo.markAs(workItem.id, InProgress)) shouldBe true
      await(repo.complete(workItem.id)) shouldBe true

      await(repo.findById(workItem.id)) shouldBe None
    }

    "not complete a vat change request if it is not in progress" in {
      val workItem = await(repo.pushNew(vatChangeEvent, anInstant))
      await(repo.complete(workItem.id)) shouldBe false
      await(repo.findById(workItem.id)) shouldBe Some(workItem)
    }

    "not complete a vat change request if it cannot be found" in {
      await(repo.complete(BSONObjectID.generate)) shouldBe false
    }
  }
}
