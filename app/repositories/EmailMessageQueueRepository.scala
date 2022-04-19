/*
 * Copyright 2022 HM Revenue & Customs
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
import models.SecureCommsMessageModel
import org.bson.types.ObjectId
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.{IndexModel, IndexOptions, Indexes}
import uk.gov.hmrc.mongo.{MongoComponent, MongoUtils}
import uk.gov.hmrc.mongo.workitem.{WorkItem, WorkItemFields, WorkItemRepository}
import uk.gov.hmrc.mongo.workitem.ProcessingStatus._

import java.time.{Duration, Instant}
import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EmailMessageQueueRepository @Inject()(appConfig: AppConfig, mongoComponent: MongoComponent)
                                           (implicit ec: ExecutionContext)
  extends WorkItemRepository[SecureCommsMessageModel](
    "EmailMessageQueue",
    mongoComponent,
    SecureCommsMessageModel.format,
    WorkItemFields.default
  ) {

  override lazy val inProgressRetryAfter: Duration = Duration.ofMillis(appConfig.retryIntervalMillis)

  override def now: Instant = Instant.now()

  override def ensureIndexes: Future[Seq[String]] =
    MongoUtils.ensureIndexes(
      collection,
      Seq(IndexModel(
        Indexes.ascending("receivedAt"),
        IndexOptions().name("workItemExpiry").expireAfter(appConfig.queueItemExpirySeconds, TimeUnit.SECONDS)
      )),
      replaceIndexes = true
    )

  def pushNew(item: SecureCommsMessageModel, receivedAt: Instant): Future[WorkItem[SecureCommsMessageModel]] =
    super.pushNew(item, receivedAt)

  def pullOutstanding: Future[Option[WorkItem[SecureCommsMessageModel]]] =
    super.pullOutstanding(now.minusMillis(appConfig.retryIntervalMillis.toInt), now)

  def complete(id: ObjectId): Future[Boolean] = {
    val query = and(equal("_id", id), equal("status", InProgress.name))
    collection.deleteOne(query).toFuture().map(_.getDeletedCount > 0)
  }
}
