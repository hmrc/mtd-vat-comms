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
