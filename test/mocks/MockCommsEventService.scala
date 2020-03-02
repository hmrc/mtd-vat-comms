/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package mocks

import base.BaseSpec
import models.VatChangeEvent
import org.scalamock.handlers.{CallHandler0, CallHandler1, CallHandler2}
import org.scalamock.scalatest.MockFactory
import services.CommsEventService
import uk.gov.hmrc.workitem.WorkItem

import scala.concurrent.Future

trait MockCommsEventService extends BaseSpec with MockFactory {

  lazy val mockCommsEventService: CommsEventService = mock[CommsEventService]

  def mockQueueRequest(data: VatChangeEvent)
                      (response: Future[Boolean]): CallHandler1[VatChangeEvent, Future[Boolean]] =
    (mockCommsEventService.queueRequest(_: VatChangeEvent))
      .expects(data)
      .returning(response)

  def mockRetrieveWorkItems(response: Future[Seq[VatChangeEvent]]): CallHandler0[Future[Seq[VatChangeEvent]]] =
    (mockCommsEventService.retrieveWorkItems _)
      .expects()
      .returning(response)

  def mockProcessWorkItems(accData: Seq[VatChangeEvent], itemData: WorkItem[VatChangeEvent])
                          (response: Future[Seq[VatChangeEvent]]): CallHandler2[Seq[VatChangeEvent], WorkItem[VatChangeEvent], Future[Seq[VatChangeEvent]]] =
    (mockCommsEventService.processWorkItem(_: Seq[VatChangeEvent], _: WorkItem[VatChangeEvent]))
      .expects(accData, itemData)
      .returning(response)
}
