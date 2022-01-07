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
