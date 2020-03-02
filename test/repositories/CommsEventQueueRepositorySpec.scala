/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package repositories

import common.ApiConstants.vatChangeEventModel
import models.VatChangeEvent
import org.joda.time.{DateTime, Duration}
import org.scalatest.BeforeAndAfterEach
import reactivemongo.api.ReadPreference
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem._

class CommsEventQueueRepositorySpec extends MongoSpec[VatChangeEvent, CommsEventQueueRepository] with BeforeAndAfterEach {
  val anInstant: DateTime = DateTimeUtils.now
  override implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

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


      requests(0) should have(
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
