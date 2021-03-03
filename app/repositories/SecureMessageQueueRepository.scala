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

package repositories

import config.{AppConfig, ConfigKeys}
import javax.inject.{Inject, Singleton}
import models.SecureCommsMessageModel
import org.joda.time.{DateTime, Duration}
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.ImplicitBSONHandlers._
import uk.gov.hmrc.mongo.json.ReactiveMongoFormats
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem.{InProgress, WorkItem, WorkItemFieldNames, WorkItemRepository}
import utils.LoggerUtil.{logDebug, logError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class SecureMessageQueueRepository @Inject()(appConfig: AppConfig, reactiveMongoComponent: ReactiveMongoComponent)
  extends WorkItemRepository[SecureCommsMessageModel, BSONObjectID](
    "SecureMessageQueue",
    reactiveMongoComponent.mongoConnector.db,
    WorkItem.workItemMongoFormat[SecureCommsMessageModel],
    appConfig.configuration.underlying
  ) {

  override val inProgressRetryAfterProperty: String = ConfigKeys.failureRetryAfterProperty

  override def now: DateTime = DateTimeUtils.now

  override lazy val workItemFields: WorkItemFieldNames = new WorkItemFieldNames {
    val receivedAt = "receivedAt"
    val updatedAt = "updatedAt"
    val availableAt = "receivedAt"
    val status = "status"
    val id = "_id"
    val failureCount = "failureCount"
  }

  val fieldName = "receivedAt"
  val createdIndexName = "workItemExpiry"
  val expireAfterSeconds = "expireAfterSeconds"
  lazy val timeToLiveInSeconds: Int = appConfig.queueItemExpirySeconds

  createIndex(fieldName, createdIndexName, timeToLiveInSeconds)

  private def createIndex(field: String, indexName: String, ttl: Int): Future[Boolean] = {
    collection.indexesManager.ensure(Index(Seq((field, IndexType.Ascending)), Some(indexName),
      options = BSONDocument(expireAfterSeconds -> ttl))) map {
      result =>
        logDebug(s"set [$indexName] with value $ttl -> result : $result")
        result
    } recover {
      case e =>
        logError("Failed to set TTL index", e)
        false
    }
  }

  def pushNew(item: SecureCommsMessageModel, receivedAt: DateTime)
                      : Future[WorkItem[SecureCommsMessageModel]] =
    super.pushNew(item, receivedAt)

  override lazy val inProgressRetryAfter: Duration = Duration.millis(appConfig.retryIntervalMillis)

  def pullOutstanding: Future[Option[WorkItem[SecureCommsMessageModel]]] =
    super.pullOutstanding(now.minusMillis(appConfig.retryIntervalMillis.toInt), now)

  def complete(id: BSONObjectID): Future[Boolean] = {
    val selector = JsObject(
      Seq("_id" -> Json.toJson(id)(ReactiveMongoFormats.objectIdFormats), "status" -> Json.toJson(InProgress)))
    collection.delete().one(selector).map(_.n > 0)
  }
}
