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

package services

import akka.actor.ActorSystem
import config.AppConfig
import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.play.scheduling.ExclusiveScheduledJob
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import utils.LoggerUtil._
import scala.util.{Failure, Success}

@Singleton
class CommsEventQueuePollingService @Inject()(actorSystem: ActorSystem,
                                    appConfig: AppConfig,
                                    repositoryAccessService: RepositoryAccessService)
                                    (implicit ec: ExecutionContext) extends ExclusiveScheduledJob {

  override def name: String = "CommsEventQueuePollingService"

  override def executeInMutex(implicit ec: ExecutionContext): Future[Result] = {
    repositoryAccessService.retrieveWorkItems.map(items => Result(s"Processed ${items.size} comms events"))
  }

  lazy val initialDelay: FiniteDuration = appConfig.initialWaitTime.seconds
  lazy val interval: FiniteDuration = appConfig.queuePollingWaitTime.seconds

  logInfo(s"Starting comms event queue scheduler." +
    s"\nInitial delay: $initialDelay" +
    s"\nPolling interval: $interval")

  def executor(): Unit = {
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
