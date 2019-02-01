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

package repositories

import config.AppConfig
import javax.inject.{Inject, Singleton}
import org.joda.time.{DateTime, Duration}
import play.api.libs.json.{Format, JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.ImplicitBSONHandlers._
import models.VatChangeEvent
import uk.gov.hmrc.mongo.json.ReactiveMongoFormats
import uk.gov.hmrc.time.DateTimeUtils
import uk.gov.hmrc.workitem._

import scala.concurrent.{ExecutionContext, Future}

object MongoPayloadDetailsFormats {
  val formats: Format[WorkItem[VatChangeEvent]] = WorkItem.workItemMongoFormat[VatChangeEvent]
}

@Singleton
class CommsEventQueueRepository @Inject()(appConfig: AppConfig, reactiveMongoComponent: ReactiveMongoComponent)
  extends WorkItemRepository[VatChangeEvent, BSONObjectID](
    "CommsEventQueue",
    reactiveMongoComponent.mongoConnector.db,
    MongoPayloadDetailsFormats.formats) {

    val inProgressRetryAfterProperty: String = ""

    override def now: DateTime = DateTimeUtils.now

    override lazy val workItemFields: WorkItemFieldNames = new WorkItemFieldNames {
      val receivedAt   = "receivedAt"
      val updatedAt    = "updatedAt"
      val availableAt  = "receivedAt"
      val status       = "status"
      val id           = "_id"
      val failureCount = "failureCount"
    }

    override lazy val inProgressRetryAfter: Duration = Duration.millis(appConfig.retryIntervalMillis)

    def pullOutstanding(implicit ec: ExecutionContext): Future[Option[WorkItem[VatChangeEvent]]] =
      super.pullOutstanding(now.minusMillis(appConfig.retryIntervalMillis.toInt), now)

    def complete(id: BSONObjectID)(implicit ec: ExecutionContext): Future[Boolean] = {
      val selector = JsObject(
        Seq("_id" -> Json.toJson(id)(ReactiveMongoFormats.objectIdFormats), "status" -> Json.toJson(InProgress)))
      collection.remove(selector).map(_.n > 0)
    }
}
