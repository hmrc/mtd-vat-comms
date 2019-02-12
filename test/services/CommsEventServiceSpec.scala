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
import metrics.QueueMetrics
import models._
import org.joda.time.{DateTime, DateTimeZone}
import repositories.CommsEventQueueRepository
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{never, times, verify, when}
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.mockito.MockitoSugar
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.workitem.{InProgress, WorkItem}
import utils.SecureCommsMessageTestData.Responses._

import scala.concurrent.Future

class CommsEventServiceSpec extends BaseSpec with MockitoSugar {

  "The queueRequest function" should {

    "add an item to the repository" in new TestSetup {
      when(queue.pushNew(any(), any())(any())).thenReturn(Future(exampleWorkItem))
      await(commsEventService.queueRequest(exampleVatChangeEvent)) shouldBe true
    }
  }

  "The retrieveWorkItems function" when {

    "a secure comms model is retrieved from the SecureCommsAlertService" when {

      "there is a transactor email in the model" when {

        "the email queue repository is successfully populated" should {

          "also populate the secure message repository" in new TestSetup {
            secureCommsAlertMock(Right(expectedResponseEverything))
            when(emailMessageService.queueRequest(expectedResponseEverything)).thenReturn(Future(true))
            when(secureMessageService.queueRequest(expectedResponseEverything)).thenReturn(Future(true))
            completeItemMock(true)

            await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(emailMessageService, times(1)).queueRequest(expectedResponseEverything)
            verify(secureMessageService, times(1)).queueRequest(expectedResponseEverything)
          }
        }

        "the email queue repository is not able to be populated" should {

          "mark the item as failed and not attempt to populate the secure message repository" in new TestSetup {
            secureCommsAlertMock(Right(expectedResponseEverything))
            when(emailMessageService.queueRequest(expectedResponseEverything)).thenReturn(Future(false))
            markItemAsFailedMock

            await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(secureMessageService, never()).queueRequest(any())
          }
        }
      }

      "there is no transactor email in the model" should {

        "attempt to populate only the secure message repository" in new TestSetup {
          secureCommsAlertMock(Right(expectedResponseNoTransactor))
          when(secureMessageService.queueRequest(expectedResponseNoTransactor)).thenReturn(Future(true))
          completeItemMock(true)

          await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

          verify(secureMessageService, times(1)).queueRequest(expectedResponseNoTransactor)
          verify(emailMessageService, never()).queueRequest(any())
        }
      }
    }

    "there is a parsing error when parsing JSON to a model" should {

      "remove the work item from the queue" in new TestSetup {
        secureCommsAlertMock(Left(GenericParsingError))
        completeItemMock(true)

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, times(1)).complete(any())(any())
      }
    }

    "there is a parsing error when parsing the secure comms string to JSON" should {

      "remove the work item from the queue" in new TestSetup {
        secureCommsAlertMock(Left(JsonParsingError))
        completeItemMock(true)

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, times(1)).complete(any())(any())
      }
    }

    "there is an unexpected error" should {

      "mark the item as failed and not remove it from the queue" in new TestSetup {
        secureCommsAlertMock(Left(ErrorModel("INTERNAL_SERVER_ERROR", "Internal server error oops")))
        markItemAsFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never()).complete(any())(any())
      }
    }
  }

  trait TestSetup {
    val now: DateTime = new DateTime(0, DateTimeZone.UTC)
    val exampleVatChangeEvent: VatChangeEvent = vatChangeEventModel("Email Address Change")
    val exampleWorkItem: WorkItem[VatChangeEvent] =
      WorkItem[VatChangeEvent](BSONObjectID.generate, now, now, now, InProgress, 0, exampleVatChangeEvent)
    val queue: CommsEventQueueRepository = mock[CommsEventQueueRepository]
    val secureCommsAlertService: SecureCommsAlertService = mock[SecureCommsAlertService]
    val emailMessageService: EmailMessageService = mock[EmailMessageService]
    val secureMessageService: SecureMessageService = mock[SecureMessageService]
    val metrics: QueueMetrics = mock[QueueMetrics]

    def secureCommsAlertMock(response: Either[ErrorModel, SecureCommsMessageModel]):
      OngoingStubbing[Future[Either[ErrorModel, SecureCommsMessageModel]]] =
        when(secureCommsAlertService.getSecureCommsMessage(any(), any(), any())(any()))
          .thenReturn(Future.successful(response))

    def completeItemMock(response: Boolean): OngoingStubbing[Future[Boolean]] =
      when(queue.complete(any())(any())).thenReturn(Future.successful(response))

    def markItemAsFailedMock: OngoingStubbing[Future[Boolean]] =
      when(queue.markAs(any(), any(), any())(any())).thenReturn(Future.successful(true))

    lazy val commsEventService =
      new CommsEventService(queue, secureCommsAlertService, emailMessageService, secureMessageService, metrics)
  }
}
