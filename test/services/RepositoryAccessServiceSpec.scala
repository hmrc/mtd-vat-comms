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
import config.AppConfig
import models.VatChangeEvent
import org.joda.time.{DateTime, DateTimeZone}
import org.mockito.ArgumentMatchers
import repositories.CommsEventQueueRepository
import org.mockito.Mockito.when
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import play.api.Configuration
import play.api.mvc.Results
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.mongo.MongoSpecSupport
import uk.gov.hmrc.workitem.{InProgress, WorkItem}
import scala.concurrent.Future

class RepositoryAccessServiceSpec
    extends WordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with Results
    with MongoSpecSupport
    with IntegrationPatience
    with BaseSpec {

  "The service" should {

    "queue a request when calling queueRequest" in new TestSetup {
      repositoryAccessService.queueRequest(exampleVatChangeEvent).futureValue shouldBe true
    }

    "process a work item from the queue" in new TestSetup {
      repositoryAccessService.processWorkItem(Seq(exampleVatChangeEvent), exampleWorkItem).futureValue shouldBe
        Seq(exampleVatChangeEvent)
    }
  }

  trait TestSetup {

    implicit val hc: HeaderCarrier = HeaderCarrier()
    val now: DateTime = new DateTime(0, DateTimeZone.UTC)
    val exampleVatChangeEvent: VatChangeEvent = VatChangeEvent("Approved", "34567542", "Email Address Change")
    val exampleWorkItem: WorkItem[VatChangeEvent] =
      WorkItem[VatChangeEvent](BSONObjectID.generate,
        now, now, now, InProgress, 0, exampleVatChangeEvent)

    val mockAppConfig: AppConfig = mock[AppConfig]
    val queue: CommsEventQueueRepository = mock[CommsEventQueueRepository]
    val configuration: Configuration = Configuration()

    when(queue.pullOutstanding(ArgumentMatchers.any(),
      ArgumentMatchers.any())(ArgumentMatchers.any())).thenReturn(Future(Some(exampleWorkItem)))

    when(queue.pushNew(ArgumentMatchers.any(), ArgumentMatchers.any())(ArgumentMatchers.any())).thenReturn(Future(exampleWorkItem))

    when(queue.complete(ArgumentMatchers.any())(ArgumentMatchers.any())).thenReturn(Future(true))

    lazy val repositoryAccessService = new RepositoryAccessService(configuration, queue)
  }

}
