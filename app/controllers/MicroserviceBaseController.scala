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

import models.ErrorModel
import play.api.libs.json.{JsError, JsSuccess, Reads}
import play.api.mvc.{AnyContentAsJson, Request}
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import utils.LoggerUtil

trait MicroserviceBaseController extends BaseController {

  def parseJsonBody[T](implicit request: Request[_], rds: Reads[T]): Either[ErrorModel, T] = request.body match {
    case body: AnyContentAsJson => body.json.validate[T] match {
      case e: JsError =>
        LoggerUtil.logWarn(s"[MicroserviceBaseController][parseJsonBody] Json received, but did not validate. Errors: $e")
        Left(ErrorModel("INVALID_JSON", s"Json received, but did not validate"))
      case s: JsSuccess[T] =>
        Right(s.value)
    }
    case _ =>
      LoggerUtil.logWarn("[MicroserviceBaseController][parseJsonBody] Body of request was not JSON")
      Left(ErrorModel("INVALID_FORMAT", s"Body of request was not JSON, ${request.body}"))
  }
}
