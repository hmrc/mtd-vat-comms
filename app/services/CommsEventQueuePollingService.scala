/*
 * Copyright 2024 HM Revenue & Customs
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

import org.apache.pekko.actor.ActorSystem
import com.google.inject.{Inject, Singleton}
import config.AppConfig
import utils.ExclusiveScheduledJob
import utils.LoggerUtil

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class CommsEventQueuePollingService @Inject()(actorSystem: ActorSystem,
                                              appConfig: AppConfig,
                                              commsEventService: CommsEventService)(
                                              implicit ec: ExecutionContext) extends ExclusiveScheduledJob with LoggerUtil {

  override def name: String = "CommsEventQueuePollingService"

  override def executeInMutex(implicit ec: ExecutionContext): Future[Result] =
    commsEventService.retrieveWorkItems.map(items => Result(s"Processed ${items.size} comms events"))

  override def initialDelay: FiniteDuration = appConfig.initialWaitTime.seconds
  override def interval: FiniteDuration = appConfig.queuePollingWaitTime.seconds

  logger.info(s"Starting comms event queue scheduler." +
    s"\nInitial delay: $initialDelay" +
    s"\nPolling interval: $interval")

  def executor()(implicit ec: ExecutionContext): Unit = {
    execute.onComplete({
      case Success(Result(message)) =>
        logger.info(message)
      case Failure(throwable) =>
        logger.warn(s"Exception completing work item", throwable)
    })
  }

  actorSystem.scheduler.scheduleWithFixedDelay(initialDelay, interval) { () =>
    if (appConfig.pollingToggle) {
      executor()
    } else {
      logger.info("Polling is toggled off")
    }
  }
}
