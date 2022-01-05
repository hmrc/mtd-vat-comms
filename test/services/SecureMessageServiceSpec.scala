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

package services

import java.net.UnknownHostException

import base.BaseSpec
import metrics.QueueMetrics
import models._
import org.joda.time.{DateTime, DateTimeZone}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{never, times, verify, when}
import org.mockito.stubbing.OngoingStubbing
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import reactivemongo.bson.BSONObjectID
import repositories.SecureMessageQueueRepository
import uk.gov.hmrc.workitem.{InProgress, ProcessingStatus, WorkItem}
import utils.SecureCommsMessageTestData.SendSecureMessageModels._

import scala.concurrent.Future

class SecureMessageServiceSpec extends BaseSpec with MockitoSugar {

  "The queueRequest function" should {

    "add an item to the repository" in new TestSetup {
      when(queue.pushNew(any(), any())).thenReturn(Future(exampleWorkItem))
      await(secureMessageService.queueRequest(exampleVatChangeEvent)) shouldBe true
    }
  }

  "The retrieveWorkItems function" when {

    "a secure comms model is retrieved from the SecureCommsAlertService" when {
      "a message model is successfully parsed by the SecureCommsMessageParser" when {
        "the email request is successfully sent" should {
          "remove the item from the queue" in new TestSetup {
            secureCommsMock(Right(true))
            completeItemMock(true)
            await(secureMessageService.processWorkItem(Seq.empty, exampleWorkItem))
            verify(queue, times(1)).complete(any())
          }
        }

        "the send secure message request is unsuccessfully sent for a GenericQueueNoRetryError" should {
          "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
            secureCommsMock(Left(GenericQueueNoRetryError))
            markItemAsPermanentlyFailedMock

            await(secureMessageService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(queue, never()).complete(any())
          }
        }

        "the secure message service returns an unexpected exception" should {
          "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
            secureCommsExceptionMock()
            markItemAsPermanentlyFailedMock

            await(secureMessageService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(queue, never()).complete(any())
          }
        }

        "the send secure message request is unsuccessfully sent for a BadRequest" should {
          "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
            secureCommsMock(Left(BadRequest))
            markItemAsPermanentlyFailedMock

            await(secureMessageService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(queue, never).complete(any())
          }
        }

        "the send secure message request is unsuccessfully sent for a SpecificParsingError" should {
          "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
            secureCommsMock(Left(SpecificParsingError))
            markItemAsPermanentlyFailedMock

            await(secureMessageService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(queue, never).complete(any())
          }
        }

        "the send secure message request is unsuccessfully sent due to an invalid vat stagger code" when {
          "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
            markItemAsPermanentlyFailedMock

            await(secureMessageService.processWorkItem(Seq.empty, exampleBadStaggerWorkItem))

            verify(queue, never).complete(any())
          }
        }

        "the secure message request is unsuccessfully sent but it a recoverable error" should {

          "mark the item as failed" in new TestSetup {
            secureCommsMock(Left(ErrorModel("Some general error", "Not of the non recoverable types")))
            markItemAsFailedMock
            await(secureMessageService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(queue, never()).complete(any())
          }
        }

      }
    }

  }

  trait TestSetup {
    val now: DateTime = new DateTime(0, DateTimeZone.UTC)
    val exampleVatChangeEvent: SecureCommsMessageModel = emailValidApprovedClientRequest
    val exampleWorkItem: WorkItem[SecureCommsMessageModel] =
      WorkItem[SecureCommsMessageModel](BSONObjectID.generate, now, now, now, InProgress, 0, exampleVatChangeEvent)
    val exampleBadStaggerWorkItem: WorkItem[SecureCommsMessageModel] =
      WorkItem[SecureCommsMessageModel](BSONObjectID.generate, now, now, now, InProgress, 0, staggerInvalidCodeRequest)
    val queue: SecureMessageQueueRepository = mock[SecureMessageQueueRepository]
    val secureCommsService: SecureCommsService = mock[SecureCommsService]
    val metrics: QueueMetrics = mock[QueueMetrics]

    def secureCommsMock(response: Either[ErrorModel, Boolean]):
    OngoingStubbing[Future[Either[ErrorModel, Boolean]]] =
      when(secureCommsService.sendSecureCommsMessage(any())(any()))
        .thenReturn(Future.successful(response))

    def secureCommsExceptionMock():
    OngoingStubbing[Future[Either[ErrorModel, Boolean]]] =
      when(secureCommsService.sendSecureCommsMessage(any())(any()))
        .thenReturn(Future.failed(new UnknownHostException("some error")))

    def completeItemMock(response: Boolean): OngoingStubbing[Future[Boolean]] =
      when(queue.complete(any())).thenReturn(Future.successful(response))

    def markItemAsFailedMock: OngoingStubbing[Future[Boolean]] =
      when(queue.markAs(any(), any(), any())(any())).thenReturn(Future.successful(true))

    def markItemAsPermanentlyFailedMock: OngoingStubbing[Future[Boolean]] =
      when(queue.markAs(any(), any[ProcessingStatus], any())(any())).thenReturn(Future.successful(true))

    lazy val secureMessageService =
      new SecureMessageService(queue, secureCommsService, metrics)
  }

}
