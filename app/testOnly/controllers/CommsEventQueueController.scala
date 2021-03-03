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

package testOnly.controllers

import com.google.inject.Inject
import controllers.MicroserviceBaseController
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import repositories.CommsEventQueueRepository
import services.CommsEventQueuePollingService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.{ExecutionContext, Future}

class CommsEventQueueController @Inject()(repository: CommsEventQueueRepository,
                                          scheduler: CommsEventQueuePollingService)(
                                          implicit ec: ExecutionContext, cc: ControllerComponents)
                                          extends BackendController(cc) with MicroserviceBaseController {

  def count: Action[AnyContent] = Action.async {
    val result: Future[Int] = repository.count
    result.map(count => Ok(count.toString))
  }

  def poll: Action[AnyContent] = Action {
    scheduler.executor()
    Ok
  }
}
