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

package services

import base.BaseSpec
import common.ApiConstants.vatChangeEventModel
import models.VatChangeEvent
import org.joda.time.{DateTime, DateTimeZone}
import repositories.CommsEventQueueRepository
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.workitem.{InProgress, WorkItem}

import scala.concurrent.Future

class RepositoryAccessServiceSpec extends BaseSpec with MockitoSugar {

  "The service" should {

    "queue a request when calling queueRequest" in new TestSetup {
      await(repositoryAccessService.queueRequest(exampleVatChangeEvent)) shouldBe true
    }

    "process a work item from the queue" in new TestSetup {
      await(repositoryAccessService.processWorkItem(Seq(exampleVatChangeEvent), exampleWorkItem)) shouldBe
        Seq(exampleVatChangeEvent)
    }
  }

  trait TestSetup {

    val now: DateTime = new DateTime(0, DateTimeZone.UTC)
    val exampleVatChangeEvent: VatChangeEvent = vatChangeEventModel("Email Address Change")
    val exampleWorkItem: WorkItem[VatChangeEvent] =
      WorkItem[VatChangeEvent](BSONObjectID.generate, now, now, now, InProgress, 0, exampleVatChangeEvent)

    val queue: CommsEventQueueRepository = mock[CommsEventQueueRepository]

    when(queue.pullOutstanding(any(), any())(any())).thenReturn(Future(Some(exampleWorkItem)))

    when(queue.pushNew(any(), any())(any())).thenReturn(Future(exampleWorkItem))

    when(queue.complete(any())(any())).thenReturn(Future(true))

    lazy val repositoryAccessService = new RepositoryAccessService(queue)
  }
}
