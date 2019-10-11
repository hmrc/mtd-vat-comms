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

package controllers

import javax.inject.Inject
import models.VatChangeEvent
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent}
import services.CommsEventService
import utils.LoggerUtil

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Right

class WebAddressController @Inject()(repoAccess: CommsEventService)(implicit val ec: ExecutionContext) extends MicroserviceBaseController {

  def handleEvent: Action[AnyContent] = Action.async { implicit request =>

    parseJsonBody[VatChangeEvent] match {
      case Right(workItem) =>
        repoAccess.queueRequest(workItem) map {
          case true  => NoContent
          case false =>
            LoggerUtil.logWarn(s"[OptOutController][handleEvent] Unable to add WorkItem to Repository: ${workItem.BPContactNumber}")
            InternalServerError
        }

      case Left(error) =>
        Future.successful(BadRequest(Json.toJson(error)))
    }
  }
}
