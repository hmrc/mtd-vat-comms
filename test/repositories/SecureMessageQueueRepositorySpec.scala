/*
 * Copyright 2021 HM Revenue & Customs
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

import models.SecureCommsMessageModel
import org.joda.time.DateTime
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import reactivemongo.api.ReadPreference
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem._
import utils.SecureCommsMessageTestData.Responses.expectedResponseEverything

class SecureMessageQueueRepositorySpec extends MongoSpec[SecureCommsMessageModel, SecureMessageQueueRepository] {

  val anInstant: DateTime = DateTimeUtils.now

  "SecureMessageQueue Repository" should {

    "ensure indexes are created" in {
      await(repo.collection.indexesManager.list()).size shouldBe 5
    }

    "be able to save and reload an item" in {
      val workItem = await(repo.pushNew(expectedResponseEverything, anInstant))

      await(repo.findById(workItem.id)).get should have(
        'item (expectedResponseEverything),
        'status (ToDo),
        'receivedAt (anInstant),
        'updatedAt (workItem.updatedAt)
      )
    }

    "be able to save the same item twice" in {
      val firstItem = await(repo.pushNew(expectedResponseEverything, anInstant))
      val secondItem = await(repo.pushNew(expectedResponseEverything, anInstant))

      val requests = await(repo.findAll(ReadPreference.primaryPreferred))
      requests should have(size(2))


      requests.head should have(
        'item (expectedResponseEverything),
        'status (ToDo),
        'receivedAt (anInstant),
        'updatedAt (firstItem.updatedAt)
      )
      requests(1) should have(
        'item (expectedResponseEverything),
        'status (ToDo),
        'receivedAt (anInstant),
        'updatedAt (secondItem.updatedAt)
      )
    }

    "pull ToDo items" in {
      val payloadDetails = expectedResponseEverything
      await(repo.pushNew(payloadDetails, anInstant))

      await(repo.pullOutstanding).get should have(
        'item (payloadDetails),
        'status (InProgress)
      )
    }

    "pull nothing if no items exist" in {
      await(repo.pullOutstanding) should be(None)
    }

    "not pull items failed after the failedBefore time" in {
      val workItem = await(repo.pushNew(expectedResponseEverything, anInstant))
      await(repo.markAs(workItem.id, Failed)) shouldBe true

      await(repo.pullOutstanding) should be(None)
    }

    "complete and delete an item if it is in progress" in {
      val workItem = await(repo.pushNew(expectedResponseEverything, anInstant))
      await(repo.markAs(workItem.id, InProgress)) shouldBe true
      await(repo.complete(workItem.id)) shouldBe true

      await(repo.findById(workItem.id)) shouldBe None
    }

    "not complete an item if it is not in progress" in {

      val workItem = await(repo.pushNew(expectedResponseEverything, anInstant))
      await(repo.complete(workItem.id)) shouldBe false
      await(repo.findById(workItem.id)) shouldBe Some(workItem)
    }

    "not complete an item if it cannot be found" in {
      await(repo.complete(BSONObjectID.generate)) shouldBe false
    }
  }
}
