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

import config.{AppConfig, ConfigKeys}

import javax.inject.{Inject, Singleton}
import models.VatChangeEvent
import org.bson.types.ObjectId
import play.api.libs.json.{JsObject, JsString, Json}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.formats.MongoFormats
import uk.gov.hmrc.mongo.workitem.ProcessingStatus.InProgress
import uk.gov.hmrc.mongo.workitem.{WorkItem, WorkItemFields, WorkItemRepository}
import utils.LoggerUtil

import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class CommsEventQueueRepository @Inject()(appConfig: AppConfig, mongoComponent: MongoComponent)
  extends WorkItemRepository[VatChangeEvent](
    "CommsEventQueue",
    mongoComponent,
    WorkItem.formatForFields[VatChangeEvent],
    appConfig.configuration.underlying
  ) {

  val appLogger: LoggerUtil = new LoggerUtil{}

  lazy val inProgressRetryAfterProperty: String = ConfigKeys.failureRetryAfterProperty

//  override def now: DateTime = DateTimeUtils.now

  override lazy val workItemFields: WorkItemFields = new WorkItemFields (
    receivedAt = "receivedAt",
    updatedAt = "updatedAt",
    availableAt = "receivedAt",
    status = "status",
    id = "_id",
    failureCount = "failureCount",
    item = "item"
  )

  val fieldName = "receivedAt"
  val createdIndexName = "workItemExpiry"
  val expireAfterSeconds = "expireAfterSeconds"
  lazy val timeToLiveInSeconds: Int = appConfig.queueItemExpirySeconds

  createIndex(fieldName, createdIndexName, timeToLiveInSeconds)

  private def createIndex(field: String, indexName: String, ttl: Int): Future[Boolean] = {
    collection.indexesManager.ensure(Index(Seq((field, IndexType.Ascending)), Some(indexName),
      options = BSONDocument(expireAfterSeconds -> ttl))) map {
      result =>
        appLogger.logger.debug(s"set [$indexName] with value $ttl -> result : $result")
        result
    } recover {
      case e =>
        appLogger.logger.error("Failed to set TTL index", e)
        false
    }
  }

  def pushNew(item: VatChangeEvent, receivedAt: Instant):
  Future[WorkItem[VatChangeEvent]] = super.pushNew(item, receivedAt)

  def pullOutstanding: Future[Option[WorkItem[VatChangeEvent]]] = {
    super.pullOutstanding(now.minusMillis(appConfig.retryIntervalMillis.toInt), now)
  }

  def complete(id: ObjectId): Future[Boolean] = {
    val selector = JsObject(
      Seq("_id" -> Json.toJson(id)(MongoFormats.objectIdFormat), "status" -> JsString(InProgress.name)))
    collection.delete().one(selector).map(_.n > 0)
  }
}
