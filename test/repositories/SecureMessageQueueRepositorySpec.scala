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

class SecureMessageQueueRepositorySpec extends BaseSpec with
  DefaultPlayMongoRepositorySupport[WorkItem[SecureCommsMessageModel]] {

  override lazy val repository = new SecureMessageQueueRepository(mockAppConfig, mongoComponent)

  "SecureMessageQueue Repository" should {

    "ensure indexes are created" in {
      await(repository.collection.listIndexes().toFuture()).size shouldBe 5
    }

    "be able to save and reload an item" in {
      val workItem = await(repository.pushNew(expectedResponseEverything, repository.now()))

      await(repository.findById(workItem.id)).get should have(
        Symbol("item") (expectedResponseEverything),
        Symbol("status") (ToDo)
      )
    }

    "be able to save the same item twice" in {
      val requests = {
        await(repository.pushNew(expectedResponseEverything, repository.now()))
        await(repository.pushNew(expectedResponseEverything, repository.now()))
        await(repository.collection.find().toFuture())
      }

      requests should have(size(2))

      requests.head should have(
        Symbol("item") (expectedResponseEverything),
        Symbol("status") (ToDo)
      )
      requests(1) should have(
        Symbol("item") (expectedResponseEverything),
        Symbol("status") (ToDo)
      )
    }

    "pull ToDo items" in {
      val payloadDetails = expectedResponseEverything
      await(repository.pushNew(payloadDetails, repository.now()))

      await(repository.pullOutstanding).get should have(
        Symbol("item") (payloadDetails),
        Symbol("status") (InProgress)
      )
    }

    "pull nothing if no items exist" in {
      await(repository.pullOutstanding) shouldBe None
    }

    "not pull items failed after the failedBefore time" in {
      val workItem = await(repository.pushNew(expectedResponseEverything, repository.now()))
      await(repository.markAs(workItem.id, Failed)) shouldBe true
      await(repository.pullOutstanding) shouldBe None
    }

    "complete and delete an item if it is in progress" in {
      val workItem = await(repository.pushNew(expectedResponseEverything, repository.now()))
      await(repository.markAs(workItem.id, InProgress)) shouldBe true
      await(repository.complete(workItem.id)) shouldBe true
      await(repository.findById(workItem.id)) shouldBe None
      await(repository.count(InProgress)) shouldBe 0
    }

    "not complete an item if it is not in progress" in {
      val workItem = await(repository.pushNew(expectedResponseEverything, repository.now()))
      await(repository.complete(workItem.id)) shouldBe false
      await(repository.count(ToDo)) shouldBe 1
    }

    "not complete an item if it cannot be found" in {
      await(repository.complete(ObjectId.get())) shouldBe false
    }
  }
}
