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

package services

import base.BaseSpec
import common.ApiConstants.vatChangeEventModel
import metrics.QueueMetrics
import models._
import org.bson.types.ObjectId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{never, times, verify, when}
import org.mockito.stubbing.OngoingStubbing
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import repositories.CommsEventQueueRepository
import uk.gov.hmrc.http.GatewayTimeoutException
import uk.gov.hmrc.mongo.workitem.ProcessingStatus.InProgress
import uk.gov.hmrc.mongo.workitem.{ProcessingStatus, WorkItem}
import utils.SecureCommsMessageTestData.Responses._

import java.net.UnknownHostException
import java.time.Instant
import scala.concurrent.Future

class CommsEventServiceSpec extends BaseSpec with MockitoSugar {

  "The queueRequest function" should {

    "add an item to the repository" in new TestSetup {
      when(queue.pushNew(any(), any())).thenReturn(Future(exampleWorkItem))
      await(commsEventService.queueRequest(exampleVatChangeEvent)) shouldBe true
    }
  }

  "The retrieveWorkItems function" when {

    "a secure comms model is retrieved from the SecureCommsAlertService" when {

      "there is a transactor email in the model" when {

        "the email queue repository is successfully populated" should {

          "also populate the secure message repository if the preference is DIGITAL " in new TestSetup {
            secureCommsAlertMock(Right(expectedResponseStagger))
            when(emailMessageService.queueRequest(expectedResponseStagger)).thenReturn(Future(true))
            when(secureMessageService.queueRequest(expectedResponseStagger)).thenReturn(Future(true))
            completeItemMock(true)

            await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(emailMessageService, times(1)).queueRequest(expectedResponseStagger)
            verify(secureMessageService, times(1)).queueRequest(expectedResponseStagger)
            verify(metrics, times(1)).commsEventDequeued()
          }

          "not populate the secure message repository if the preference is PAPER" in new TestSetup {
            secureCommsAlertMock(Right(expectedResponseStaggerPaper))
            when(emailMessageService.queueRequest(expectedResponseStaggerPaper)).thenReturn(Future(true))
            completeItemMock(true)

            await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(emailMessageService, times(1)).queueRequest(expectedResponseStaggerPaper)
            verify(secureMessageService, times(0)).queueRequest(expectedResponseStaggerPaper)
            verify(metrics, times(1)).commsEventDequeued()
          }

        }

        "the email queue repository is not able to be populated" should {

          "mark the item as failed and not attempt to populate the secure message repository" in new TestSetup {
            secureCommsAlertMock(Right(expectedResponseStagger))
            when(emailMessageService.queueRequest(expectedResponseStagger)).thenReturn(Future(false))
            markItemAsFailedMock

            await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(secureMessageService, never).queueRequest(any())
            verify(metrics, times(1)).commsEventQueuedForRetry()
          }
        }
      }

      "there is an original client email in the model indicating a change of email" should {

        "populate both the email repository and secure message repository" in new TestSetup {
          secureCommsAlertMock(Right(expectedResponseEmailChange))
          when(emailMessageService.queueRequest(expectedResponseEmailChange)).thenReturn(Future(true))
          when(secureMessageService.queueRequest(expectedResponseEmailChange)).thenReturn(Future(true))
          completeItemMock(true)

          await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

          verify(emailMessageService, times(1)).queueRequest(expectedResponseEmailChange)
          verify(secureMessageService, times(1)).queueRequest(expectedResponseEmailChange)
          verify(metrics, times(1)).commsEventDequeued()
        }
      }

      "there is no transactor email in the model" should {

        "attempt to populate only the secure message repository" in new TestSetup {
          secureCommsAlertMock(Right(expectedResponseNoTransactor))
          when(secureMessageService.queueRequest(expectedResponseNoTransactor)).thenReturn(Future(true))
          completeItemMock(true)

          await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

          verify(secureMessageService, times(1)).queueRequest(expectedResponseNoTransactor)
          verify(emailMessageService, never).queueRequest(any())
          verify(metrics, times(1)).commsEventDequeued()
        }
      }
    }

    "there is a parsing error when parsing JSON to a model" should {

      "remove the work item from the queue" in new TestSetup {
        secureCommsAlertMock(Left(GenericParsingError))
        markItemAsPermanentlyFailedMock
        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())
        verify(metrics, times(1)).commsEventGenericParsingError()
      }
    }

    "there is a parsing error when parsing the secure comms string to JSON" should {

      "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
        secureCommsAlertMock(Left(JsonParsingError))
        markItemAsPermanentlyFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())
        verify(metrics, times(1)).commsEventJsonParsingError()
      }
    }

    "there is a not found no match" should {

      "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
        secureCommsAlertMock(Left(NotFoundNoMatch))
        markItemAsPermanentlyFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())
        verify(metrics, times(1)).commsEventNotFoundError()
      }
    }

    "there is a bad request" should {

      "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
        secureCommsAlertMock(Left(BadRequest))
        markItemAsPermanentlyFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())
        verify(metrics, times(1)).commsEventBadRequestError()
      }
    }

    "there is an unexpected error" should {

      "mark the item as failed and not remove it from the queue" in new TestSetup {
        secureCommsAlertMock(Left(ErrorModel("INTERNAL_SERVER_ERROR", "Internal server error oops")))
        markItemAsFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())
        verify(metrics, times(1)).commsEventQueuedForRetry()
      }
    }

    "there is a gateway timeout exception" should {

      "mark the item as failed and not remove it from the queue" in new TestSetup {
        gatewayTimeoutMock()
        markItemAsFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())
        verify(metrics, times(1)).commsEventQueuedForRetry()
      }
    }

    "there is an unexpected exception" should {

      "mark the item as permanently failed and not remove it from the queue" in new TestSetup {
        secureCommsAlertExceptionMock()
        markItemAsPermanentlyFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())
        verify(metrics, times(1)).commsEventUnexpectedError()
      }
    }
  }

  trait TestSetup {
    val now: Instant = Instant.now
    val exampleVatChangeEvent: VatChangeEvent = vatChangeEventModel("Email Address Change")
    val exampleWorkItem: WorkItem[VatChangeEvent] =
      WorkItem[VatChangeEvent](ObjectId.get(), now, now, now, InProgress, 0, exampleVatChangeEvent)
    val queue: CommsEventQueueRepository = mock[CommsEventQueueRepository]
    val secureCommsAlertService: SecureCommsAlertService = mock[SecureCommsAlertService]
    val emailMessageService: EmailMessageService = mock[EmailMessageService]
    val secureMessageService: SecureMessageService = mock[SecureMessageService]
    val metrics: QueueMetrics = mock[QueueMetrics]

    def secureCommsAlertMock(response: Either[ErrorModel, SecureCommsMessageModel]):
      OngoingStubbing[Future[Either[ErrorModel, SecureCommsMessageModel]]] =
        when(secureCommsAlertService.getSecureCommsMessage(any(), any(), any())(any()))
          .thenReturn(Future.successful(response))

    def secureCommsAlertExceptionMock() : OngoingStubbing[Future[Either[ErrorModel, SecureCommsMessageModel]]] =
      when(secureCommsAlertService.getSecureCommsMessage(any(), any(), any())(any()))
        .thenReturn(Future.failed(new UnknownHostException("some error")))

    def gatewayTimeoutMock(): OngoingStubbing[Future[Either[ErrorModel, SecureCommsMessageModel]]] =
      when(secureCommsAlertService.getSecureCommsMessage(any(), any(), any())(any()))
        .thenReturn(Future.failed(new GatewayTimeoutException("some error")))

    def completeItemMock(response: Boolean): OngoingStubbing[Future[Boolean]] =
      when(queue.complete(any())).thenReturn(Future.successful(response))

    def markItemAsPermanentlyFailedMock: OngoingStubbing[Future[Boolean]] =
      when(queue.markAs(any(), any[ProcessingStatus], any())).thenReturn(Future.successful(true))

    def markItemAsFailedMock: OngoingStubbing[Future[Boolean]] =
      when(queue.markAs(any(), any(), any())).thenReturn(Future.successful(true))

    lazy val commsEventService =
      new CommsEventService(queue, secureCommsAlertService, emailMessageService, secureMessageService, metrics)
  }
}
