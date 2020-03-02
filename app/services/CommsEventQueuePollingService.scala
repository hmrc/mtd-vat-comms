/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import akka.actor.ActorSystem
import config.AppConfig
import com.google.inject.{Inject, Singleton}
import uk.gov.hmrc.play.scheduling.ExclusiveScheduledJob

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import utils.LoggerUtil._

import scala.util.{Failure, Success}

@Singleton
class CommsEventQueuePollingService @Inject()(actorSystem: ActorSystem,
                                              appConfig: AppConfig,
                                              commsEventService: CommsEventService)(
                                              implicit ec: ExecutionContext) extends ExclusiveScheduledJob {

  override def name: String = "CommsEventQueuePollingService"

  override def executeInMutex(implicit ec: ExecutionContext): Future[Result] =
    commsEventService.retrieveWorkItems.map(items => Result(s"Processed ${items.size} comms events"))

  lazy val initialDelay: FiniteDuration = appConfig.initialWaitTime.seconds
  lazy val interval: FiniteDuration = appConfig.queuePollingWaitTime.seconds

  logInfo(s"Starting comms event queue scheduler." +
    s"\nInitial delay: $initialDelay" +
    s"\nPolling interval: $interval")

  def executor()(implicit ec: ExecutionContext): Unit = {
    execute.onComplete({
      case Success(Result(_)) =>
        logInfo(_)
      case Failure(throwable) =>
        logWarn(s"Exception completing work item", throwable)
    })
  }

  actorSystem.scheduler.schedule(initialDelay, interval) {
    if (appConfig.pollingToggle) {
      executor()
    } else {
      logInfo("Polling is toggled off")
    }
  }
}
