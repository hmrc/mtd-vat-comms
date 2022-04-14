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

import base.BaseSpec
import models.SecureCommsMessageModel
import org.bson.types.ObjectId
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport
import uk.gov.hmrc.mongo.workitem.ProcessingStatus._
import uk.gov.hmrc.mongo.workitem.WorkItem
import utils.SecureCommsMessageTestData.Responses.expectedResponseEverything

import java.time.Instant

class SecureMessageQueueRepositorySpec extends BaseSpec with
  DefaultPlayMongoRepositorySupport[WorkItem[SecureCommsMessageModel]] {

  override lazy val repository = new SecureMessageQueueRepository(mockAppConfig, mongoComponent)
  val now: Instant = Instant.now

  "SecureMessageQueue Repository" should {

    "ensure indexes are created" in {
      await(repository.collection.listIndexes().toFuture()).size shouldBe 5
    }

    "be able to save and reload an item" in {
      val workItem = await(repository.pushNew(expectedResponseEverything, now))

      await(repository.findById(workItem.id)).get should have(
        'item (expectedResponseEverything),
        'status (ToDo),
        'receivedAt (now),
        'updatedAt (workItem.updatedAt)
      )
    }

    "be able to save the same item twice" in {
      val firstItem = await(repository.pushNew(expectedResponseEverything, now))
      val secondItem = await(repository.pushNew(expectedResponseEverything, now))
      val requests = await(repository.collection.find().toFuture())

      requests should have(size(2))

      requests.head should have(
        'item (expectedResponseEverything),
        'status (ToDo),
        'receivedAt (now),
        'updatedAt (firstItem.updatedAt)
      )
      requests(1) should have(
        'item (expectedResponseEverything),
        'status (ToDo),
        'receivedAt (now),
        'updatedAt (secondItem.updatedAt)
      )
    }

    "pull ToDo items" in {
      val payloadDetails = expectedResponseEverything
      await(repository.pushNew(payloadDetails, repository.now))

      await(repository.pullOutstanding).get should have(
        'item (payloadDetails),
        'status (InProgress)
      )
    }

    "pull nothing if no items exist" in {
      await(repository.pullOutstanding) should be(None)
    }

    "not pull items failed after the failedBefore time" in {
      val workItem = await(repository.pushNew(expectedResponseEverything, repository.now))
      await(repository.markAs(workItem.id, Failed)) shouldBe true
      await(repository.pullOutstanding) shouldBe None
    }

    "complete and delete an item if it is in progress" in {
      val workItem = await(repository.pushNew(expectedResponseEverything, repository.now))
      await(repository.markAs(workItem.id, InProgress)) shouldBe true
      await(repository.complete(workItem.id)) shouldBe true
      await(repository.findById(workItem.id)) shouldBe None
    }

    "not complete an item if it is not in progress" in {
      val workItem = await(repository.pushNew(expectedResponseEverything, repository.now))
      await(repository.complete(workItem.id)) shouldBe false
      await(repository.findById(workItem.id)) shouldBe Some(workItem)
    }

    "not complete an item if it cannot be found" in {
      await(repository.complete(ObjectId.get())) shouldBe false
    }
  }
}
