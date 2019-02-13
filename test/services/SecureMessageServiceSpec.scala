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
import models.SecureCommsMessageModel
import org.joda.time.{DateTime, DateTimeZone}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import reactivemongo.bson.BSONObjectID
import repositories.SecureMessageQueueRepository
import uk.gov.hmrc.workitem.{InProgress, WorkItem}
import utils.SecureCommsMessageTestData.Responses.expectedResponseEverything

import scala.concurrent.Future

class SecureMessageServiceSpec extends BaseSpec with MockitoSugar {

  "EmailMessageService" should {

    "queue a request when calling queueRequest" in new TestSetup {
      await(secureMessageService.queueRequest(exampleSecureCommsModel)) shouldBe true
    }
  }

  trait TestSetup {

    val now: DateTime = new DateTime(0, DateTimeZone.UTC)
    val exampleSecureCommsModel: SecureCommsMessageModel = expectedResponseEverything
    val exampleWorkItem: WorkItem[SecureCommsMessageModel] =
      WorkItem[SecureCommsMessageModel](BSONObjectID.generate, now, now, now, InProgress, 0, exampleSecureCommsModel)
    val queue: SecureMessageQueueRepository = mock[SecureMessageQueueRepository]

    when(queue.pushNew(any(), any())(any())).thenReturn(Future(exampleWorkItem))

    lazy val secureMessageService = new SecureMessageService(queue)
  }
}
