/*
 * Copyright 2020 HM Revenue & Customs
 *
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
            LoggerUtil.logWarn(s"[WebAddressController][handleEvent] Unable to add WorkItem to Repository: ${workItem.BPContactNumber}")
            InternalServerError
        }

      case Left(error) =>
        Future.successful(BadRequest(Json.toJson(error)))
    }
  }
}
