/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import akka.actor.ActorSystem
import com.google.inject.{Inject, Singleton}
import config.AppConfig
import uk.gov.hmrc.play.scheduling.ExclusiveScheduledJob
import utils.LoggerUtil._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class SecureMessageQueuePollingService @Inject()(actorSystem: ActorSystem,
                                                 appConfig: AppConfig,
                                                 secureMessageService: SecureMessageService)(
                                                 implicit ec: ExecutionContext) extends ExclusiveScheduledJob {

  override def name: String = "SecureMessageQueuePollingService"

  override def executeInMutex(implicit ec: ExecutionContext): Future[Result] =
    secureMessageService.retrieveWorkItems.map(items => Result(s"Processed ${items.size} secure message events"))

  lazy val initialDelay: FiniteDuration = appConfig.initialWaitTime.seconds
  lazy val interval: FiniteDuration = appConfig.queuePollingWaitTime.seconds

  logInfo(s"Starting secure message event queue scheduler." +
    s"\nInitial delay: $initialDelay" +
    s"\nPolling interval: $interval")

  def executor()(implicit ec: ExecutionContext): Unit = {
    execute.onComplete({
      case Success(Result(_)) =>
        logInfo(_)
      case Failure(throwable) =>
        logError(s"Exception completing work item", throwable)
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
