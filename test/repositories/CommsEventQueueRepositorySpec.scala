/*
 * Copyright 2023 HM Revenue & Customs
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
import common.ApiConstants.vatChangeEventModel
import models.VatChangeEvent
import org.bson.types.ObjectId
import org.mongodb.scala.bson.{BsonInt32, BsonString}
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport
import uk.gov.hmrc.mongo.workitem.WorkItem
import uk.gov.hmrc.mongo.workitem.ProcessingStatus._

class CommsEventQueueRepositorySpec extends BaseSpec with DefaultPlayMongoRepositorySupport[WorkItem[VatChangeEvent]] {

  override lazy val repository = new CommsEventQueueRepository(mockAppConfig, mongoComponent)

  "CommsEventQueue Repository" should {

    "ensure indexes are created" in {
      await(repository.collection.listIndexes().toFuture()).size shouldBe 5
    }

    "have a TTL index on the receivedAt field, with an expiry time set by AppConfig" in {
      val indexes = {
        await(repository.ensureIndexes())
        await(repository.collection.listIndexes().toFuture())
      }
      val ttlIndex = indexes.find(_.get("name").contains(BsonString("workItemExpiry")))

      ttlIndex.get("key").toString shouldBe """{"receivedAt": 1}"""
      ttlIndex.get("expireAfterSeconds") shouldBe BsonInt32(mockAppConfig.queueItemExpirySeconds)
    }

    val vatChangeEvent: VatChangeEvent = vatChangeEventModel("PPOB Change")

    "be able to save and reload a vat change request" in {
      val workItem = await(repository.pushNew(vatChangeEvent, repository.now()))

      await(repository.findById(workItem.id)).get should have(
        Symbol("item") (vatChangeEvent),
        Symbol("status") (ToDo)
      )
    }

    "be able to save the same requests twice" in {
      val requests = {
        await(repository.pushNew(vatChangeEvent, repository.now()))
        await(repository.pushNew(vatChangeEvent, repository.now()))
        await(repository.collection.find().toFuture())
      }

      requests should have(size(2))

      requests.head should have(
        Symbol("item") (vatChangeEvent),
        Symbol("status") (ToDo)
      )
      requests(1) should have(
        Symbol("item") (vatChangeEvent),
        Symbol("status") (ToDo)
      )
    }

    "pull ToDo vat change requests" in {
      await(repository.pushNew(vatChangeEvent, repository.now()))

      await(repository.pullOutstanding).get should have(
        Symbol("item") (vatChangeEvent),
        Symbol("status") (InProgress)
      )
    }

    "pull nothing if no vat change requests exist" in {
      await(repository.pullOutstanding) shouldBe None
    }

    "not pull vat change requests failed after the failedBefore time" in {
      val workItem: WorkItem[VatChangeEvent] = await(repository.pushNew(vatChangeEvent, repository.now()))
      await(repository.markAs(workItem.id, Failed)) shouldBe true
      await(repository.pullOutstanding) shouldBe None
    }

    "complete and delete a vat change request if it is in progress" in {
      val workItem = await(repository.pushNew(vatChangeEvent, repository.now()))
      await(repository.markAs(workItem.id, InProgress)) shouldBe true
      await(repository.complete(workItem.id)) shouldBe true
      await(repository.findById(workItem.id)) shouldBe None
      await(repository.count(InProgress)) shouldBe 0
    }

    "not complete a vat change request if it is not in progress" in {
      val workItem = await(repository.pushNew(vatChangeEvent, repository.now()))
      await(repository.complete(workItem.id)) shouldBe false
      await(repository.count(ToDo)) shouldBe 1
    }

    "not complete a vat change request if it cannot be found" in {
      await(repository.complete(ObjectId.get())) shouldBe false
    }
  }
}
