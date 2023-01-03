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

package testOnly.controllers

import controllers.MicroserviceBaseController
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import repositories.SecureMessageQueueRepository
import services.SecureMessageQueuePollingService
import uk.gov.hmrc.mongo.workitem.ProcessingStatus

import javax.inject.Inject
import scala.concurrent.duration.{Duration, SECONDS}
import scala.concurrent.{Await, ExecutionContext, Future}

class SecureMessageQueueController @Inject()(repository: SecureMessageQueueRepository,
                                             scheduler: SecureMessageQueuePollingService,
                                             cc: ControllerComponents)
                                            (implicit ec: ExecutionContext) extends MicroserviceBaseController(cc) {

  def count: Action[AnyContent] = Action {
    val counts: Set[Future[Long]] = ProcessingStatus.values.map(repository.count)
    val sum = counts.map(x => Await.result(x, Duration(1, SECONDS))).sum
    Ok(sum.toString)
  }

  def poll: Action[AnyContent] = Action {
    scheduler.executor()
    Ok
  }
}
