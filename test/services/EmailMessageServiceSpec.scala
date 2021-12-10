/*
 * Copyright 2021 HM Revenue & Customs
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
import models.responseModels.EmailRendererResponseModel
import models.{BadRequest, ErrorModel, NotFoundNoMatch, SecureCommsMessageModel}
import org.joda.time.{DateTime, DateTimeZone}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{never, times, verify, when}
import org.mockito.stubbing.OngoingStubbing
import org.scalatestplus.mockito.MockitoSugar
import play.api.http.Status.ACCEPTED
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import reactivemongo.bson.BSONObjectID
import repositories.EmailMessageQueueRepository
import uk.gov.hmrc.workitem.{InProgress, ProcessingStatus, WorkItem}
import utils.SecureCommsMessageTestData.Responses._

import scala.concurrent.Future

class EmailMessageServiceSpec extends BaseSpec with MockitoSugar {

  "The queueRequest function" should {

    "add an item to the repository" in new TestSetup {
      when(queue.pushNew(any(), any())).thenReturn(Future(exampleWorkItem))
      await(emailMessageService.queueRequest(exampleSecureCommsModel)) shouldBe true
    }
  }

  "The retrieveWorkItems function" when {

    "a message model is successfully parsed by the SecureCommsMessageParser" when {

      "the email request is successfully sent" should {

        "remove the item from the queue" in new TestSetup {
          emailServiceMock(Right(EmailRendererResponseModel(ACCEPTED)))
          completeItemMock(true)

          await(emailMessageService.processWorkItem(Seq.empty, exampleWorkItem))

          verify(queue, times(1)).complete(any())
        }
      }

      "the email service returns BAD_REQUEST (400)" should {

        "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
          emailServiceMock(Left(BadRequest))
          markItemAsPermanentlyFailedMock

          await(emailMessageService.processWorkItem(Seq.empty, exampleWorkItem))

          verify(queue, never).complete(any())
        }
      }

      "the email service returns NOT_FOUND (404)" should {

        "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
          emailServiceMock(Left(NotFoundNoMatch))
          markItemAsPermanentlyFailedMock

          await(emailMessageService.processWorkItem(Seq.empty, exampleWorkItem))

          verify(queue, never).complete(any())
        }
      }

      "the email service returns an unexpected status" should {

        "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
          emailServiceMock(Left(ErrorModel("unknown", "unknown")))
          markItemAsFailedMock

          await(emailMessageService.processWorkItem(Seq.empty, exampleWorkItem))

          verify(queue, never()).complete(any())
        }
      }

      "the email service returns an future failed unexpected exception" should {

        "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
          markItemAsPermanentlyFailedMock

          emailServiceExceptionMock()

          await(emailMessageService.processWorkItem(Seq.empty, exampleWorkItem))

          verify(queue, never()).complete(any())
        }

      }
      "the email service returns a standard unexpected exception" should {

        "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
          markItemAsPermanentlyFailedMock

          emailServiceThrowExceptionMock()

          await(emailMessageService.processWorkItem(Seq.empty, exampleWorkItem))

          verify(queue, never()).complete(any())
        }

      }
    }

    "a message model is unsuccessfully parsed by the SecureCommsMessageParser" should {

      "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
        val failureWorkItem: WorkItem[SecureCommsMessageModel] =
          WorkItem[SecureCommsMessageModel](BSONObjectID.generate, now, now, now, InProgress, 0, expectedResponseEverything)
        markItemAsPermanentlyFailedMock

        await(emailMessageService.processWorkItem(Seq.empty, failureWorkItem))

        verify(queue, never).complete(any())
      }
    }
  }

  trait TestSetup {
    val now: DateTime = new DateTime(0, DateTimeZone.UTC)
    val exampleSecureCommsModel: SecureCommsMessageModel = expectedResponsePPOBChange
    val exampleWorkItem: WorkItem[SecureCommsMessageModel] =
      WorkItem[SecureCommsMessageModel](BSONObjectID.generate, now, now, now, InProgress, 0, exampleSecureCommsModel)
    val queue: EmailMessageQueueRepository = mock[EmailMessageQueueRepository]
    val emailService: EmailService = mock[EmailService]
    val metrics: QueueMetrics = mock[QueueMetrics]

    def completeItemMock(response: Boolean): OngoingStubbing[Future[Boolean]] =
      when(queue.complete(any())).thenReturn(Future.successful(response))

    def markItemAsFailedMock: OngoingStubbing[Future[Boolean]] =
      when(queue.markAs(any(), any(), any())(any())).thenReturn(Future.successful(true))

    def markItemAsPermanentlyFailedMock: OngoingStubbing[Future[Boolean]] =
      when(queue.markAs(any(), any[ProcessingStatus], any())(any())).thenReturn(Future.successful(true))

    def emailServiceMock(response: Either[ErrorModel, EmailRendererResponseModel]):
    OngoingStubbing[Future[Either[ErrorModel, EmailRendererResponseModel]]] =
      when(emailService.sendEmailRequest(any())(any()))
        .thenReturn(Future.successful(response))

    def emailServiceExceptionMock():
    OngoingStubbing[Future[Either[ErrorModel, EmailRendererResponseModel]]] =
      when(emailService.sendEmailRequest(any())(any()))
        .thenReturn(Future.failed(new UnknownHostException("some error")))

    def emailServiceThrowExceptionMock():
    OngoingStubbing[Future[Either[ErrorModel, EmailRendererResponseModel]]] =
      when(emailService.sendEmailRequest(any())(any()))
        .thenThrow(new NoSuchElementException("UnexpectedError"))

    lazy val emailMessageService = new EmailMessageService(queue, emailService, metrics)
  }
}
