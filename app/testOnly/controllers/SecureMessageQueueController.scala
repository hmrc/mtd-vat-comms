/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package testOnly.controllers

import controllers.MicroserviceBaseController
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}
import repositories.SecureMessageQueueRepository
import services.SecureMessageQueuePollingService

import scala.concurrent.{ExecutionContext, Future}

class SecureMessageQueueController @Inject()(repository: SecureMessageQueueRepository,
                                             scheduler: SecureMessageQueuePollingService)(
                                             implicit ec: ExecutionContext) extends MicroserviceBaseController {

  def count: Action[AnyContent] = Action.async { implicit request =>
    val result: Future[Int] = repository.count
    result.map(count => Ok(count.toString))
  }

  def poll: Action[AnyContent] = Action { implicit request =>
    scheduler.executor()
    Ok
  }
}
