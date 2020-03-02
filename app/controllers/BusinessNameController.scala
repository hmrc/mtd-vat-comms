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

class BusinessNameController @Inject()()(implicit val ec: ExecutionContext) extends MicroserviceBaseController {

  def handleEvent: Action[AnyContent] = Action { implicit request =>
    parseJsonBody[VatChangeEvent] match {
      case Right(_) => NoContent
      case Left(error) => BadRequest(Json.toJson(error))
    }
  }
}
