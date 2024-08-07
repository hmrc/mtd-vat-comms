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

package repositories

import config.AppConfig
import models.VatChangeEvent
import org.bson.types.ObjectId
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.{IndexModel, IndexOptions, Indexes}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.workitem.ProcessingStatus.InProgress
import uk.gov.hmrc.mongo.workitem.{WorkItem, WorkItemFields, WorkItemRepository}

import java.time.{Duration, Instant}
import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommsEventQueueRepository @Inject()(appConfig: AppConfig, mongoComponent: MongoComponent)
                                         (implicit ec: ExecutionContext)
  extends WorkItemRepository[VatChangeEvent](
    "CommsEventQueue",
    mongoComponent,
    VatChangeEvent.formats,
    WorkItemFields.default,
    extraIndexes = Seq(IndexModel(
      Indexes.ascending("receivedAt"),
      IndexOptions().name("workItemExpiry").expireAfter(appConfig.queueItemExpirySeconds, TimeUnit.SECONDS)
    )),
    replaceIndexes = true
  ) {

  override lazy val inProgressRetryAfter: Duration = Duration.ofMillis(appConfig.retryIntervalMillis)

  override def now(): Instant = Instant.now()

  def pushNew(item: VatChangeEvent, receivedAt: Instant): Future[WorkItem[VatChangeEvent]] = super.pushNew(item, receivedAt)

  def pullOutstanding: Future[Option[WorkItem[VatChangeEvent]]] =
    super.pullOutstanding(now().minusMillis(appConfig.retryIntervalMillis.toInt), now())

  def complete(id: ObjectId): Future[Boolean] = {
    val query = and(equal("_id", id), equal("status", InProgress.name))
    collection.deleteOne(query).toFuture().map(_.getDeletedCount > 0)
  }
}
