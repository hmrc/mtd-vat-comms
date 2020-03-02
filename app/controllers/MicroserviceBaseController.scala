/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import models.ErrorModel
import play.api.libs.json.{JsError, JsSuccess, Reads}
import play.api.mvc.{AnyContentAsJson, Request}
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import utils.LoggerUtil.{logWarn, logDebug}

trait MicroserviceBaseController extends BaseController {

  def parseJsonBody[T](implicit request: Request[_], rds: Reads[T]): Either[ErrorModel, T] = request.body match {
    case body: AnyContentAsJson => body.json.validate[T] match {
      case e: JsError =>
        logDebug(s"[MicroserviceBaseController][parseJsonBody] Json received, but did not validate. Errors: $e")
        logWarn(s"[MicroserviceBaseController][parseJsonBody] Json received, but did not validate.")
        Left(ErrorModel("INVALID_JSON", "Json received, but did not validate"))
      case s: JsSuccess[T] =>
        Right(s.value)
    }
    case _ =>
      logDebug(s"[MicroserviceBaseController][parseJsonBody] Body of request was not JSON. Request body received:'${request.body}'")
      logWarn(s"[MicroserviceBaseController][parseJsonBody] Body of request was not JSON.")
      Left(ErrorModel("INVALID_FORMAT", s"Request body was not JSON."))
  }
}
