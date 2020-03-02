/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.net.UnknownHostException

import base.BaseSpec
import common.ApiConstants.vatChangeEventModel
import metrics.QueueMetrics
import models._
import org.joda.time.{DateTime, DateTimeZone}
import repositories.CommsEventQueueRepository
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{never, times, verify, when}
import org.mockito.stubbing.OngoingStubbing
import org.scalatestplus.mockito.MockitoSugar
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.workitem.{InProgress, ProcessingStatus, WorkItem}
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
            secureCommsAlertMock(Right(expectedResponseStagger))
            when(emailMessageService.queueRequest(expectedResponseStagger)).thenReturn(Future(true))
            when(secureMessageService.queueRequest(expectedResponseStagger)).thenReturn(Future(true))
            completeItemMock(true)

            await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(emailMessageService, times(1)).queueRequest(expectedResponseStagger)
            verify(secureMessageService, times(1)).queueRequest(expectedResponseStagger)
          }
        }

        "the email queue repository is not able to be populated" should {

          "mark the item as failed and not attempt to populate the secure message repository" in new TestSetup {
            secureCommsAlertMock(Right(expectedResponseStagger))
            when(emailMessageService.queueRequest(expectedResponseStagger)).thenReturn(Future(false))
            markItemAsFailedMock

            await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

            verify(secureMessageService, never()).queueRequest(any())
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
        markItemAsPermanentlyFailedMock
        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())(any())
      }
    }

    "there is a parsing error when parsing the secure comms string to JSON" should {

      "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
        secureCommsAlertMock(Left(JsonParsingError))
        markItemAsPermanentlyFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())(any())
      }
    }

    "there is a not found no match" should {

      "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
        secureCommsAlertMock(Left(NotFoundNoMatch))
        markItemAsPermanentlyFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())(any())
      }
    }

    "there is a bad request" should {

      "mark the item as permanently failed and not remove the item from the queue" in new TestSetup {
        secureCommsAlertMock(Left(BadRequest))
        markItemAsPermanentlyFailedMock

        await(commsEventService.processWorkItem(Seq.empty, exampleWorkItem))

        verify(queue, never).complete(any())(any())
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

    "there is an unexpected exception" should {

      "mark the item as permanently failed and not remove it from the queue" in new TestSetup {
        secureCommsAlertExceptionMock()
        markItemAsPermanentlyFailedMock

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

    def secureCommsAlertExceptionMock():
    OngoingStubbing[Future[Either[ErrorModel, SecureCommsMessageModel]]] =
      when(secureCommsAlertService.getSecureCommsMessage(any(), any(), any())(any()))
        .thenReturn(Future.failed(new UnknownHostException("some error")))

    def completeItemMock(response: Boolean): OngoingStubbing[Future[Boolean]] =
      when(queue.complete(any())(any())).thenReturn(Future.successful(response))

    def markItemAsPermanentlyFailedMock: OngoingStubbing[Future[Boolean]] =
      when(queue.markAs(any(), any[ProcessingStatus], any())(any())).thenReturn(Future.successful(true))

    def markItemAsFailedMock: OngoingStubbing[Future[Boolean]] =
      when(queue.markAs(any(), any(), any())(any())).thenReturn(Future.successful(true))

    lazy val commsEventService =
      new CommsEventService(queue, secureCommsAlertService, emailMessageService, secureMessageService, metrics)
  }
}
