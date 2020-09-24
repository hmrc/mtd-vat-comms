/*
 * Copyright 2020 HM Revenue & Customs
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

package metrics

import com.codahale.metrics.Counter
import com.kenshoo.play.metrics.Metrics
import javax.inject.Inject

class QueueMetrics @Inject()(metrics: Metrics) {

  val commsEventEnqueuedCounter: Counter = metrics.defaultRegistry.counter("commsEvent.enqueued")
  val commsEventDequeuedCounter: Counter = metrics.defaultRegistry.counter("commsEvent.dequeued")
  val commsEventGenericParsingErrorCounter: Counter = metrics.defaultRegistry.counter("commsEvent.genericParsingError")
  val commsEventJsonParsingErrorCounter: Counter = metrics.defaultRegistry.counter("commsEvent.jsonParsingError")
  val commsEventNotFoundErrorCounter: Counter = metrics.defaultRegistry.counter("commsEvent.notFoundError")
  val commsEventBadRequestErrorCounter: Counter = metrics.defaultRegistry.counter("commsEvent.badRequestError")
  val commsEventUnexpectedErrorCounter: Counter = metrics.defaultRegistry.counter("commsEvent.unexpectedError")
  val commsEventQueuedForRetryCounter: Counter = metrics.defaultRegistry.counter("commsEvent.queuedForRetry")

  val emailMessageEnqueuedCounter: Counter = metrics.defaultRegistry.counter("emailMessage.enqueued")
  val emailMessageDequeuedCounter: Counter = metrics.defaultRegistry.counter("emailMessage.dequeued")
  val emailMessageParsingErrorCounter: Counter = metrics.defaultRegistry.counter("emailMessage.parsingError")
  val emailMessageNotFoundErrorCounter: Counter = metrics.defaultRegistry.counter("emailMessage.notFoundError")
  val emailMessageBadRequestErrorCounter: Counter = metrics.defaultRegistry.counter("emailMessage.badRequestError")
  val emailMessageUnexpectedErrorCounter: Counter = metrics.defaultRegistry.counter("emailMessage.unexpectedError")
  val emailMessageQueuedForRetryCounter: Counter = metrics.defaultRegistry.counter("emailMessage.queuedForRetry")

  val secureMessageEnqueuedCounter: Counter = metrics.defaultRegistry.counter("secureMessage.enqueued")
  val secureMessageDequeuedCounter: Counter = metrics.defaultRegistry.counter("secureMessage.dequeued")
  val secureMessageGenericQueueNoRetryErrorCounter: Counter = metrics.defaultRegistry.counter("secureMessage.genericQueueNoRetryError")
  val secureMessageBadRequestErrorCounter: Counter = metrics.defaultRegistry.counter("secureMessage.badRequestError")
  val secureMessageSpecificParsingErrorCounter: Counter = metrics.defaultRegistry.counter("secureMessage.specificParsingError")
  val secureMessageUnexpectedErrorCounter: Counter = metrics.defaultRegistry.counter("secureMessage.unexpectedError")
  val secureMessageQueuedForRetryCounter: Counter = metrics.defaultRegistry.counter("secureMessage.queuedForRetry")

  def commsEventEnqueued(): Unit = commsEventEnqueuedCounter.inc()
  def commsEventDequeued(): Unit = commsEventDequeuedCounter.inc()
  def commsEventGenericParsingError(): Unit = commsEventGenericParsingErrorCounter.inc()
  def commsEventJsonParsingError(): Unit = commsEventJsonParsingErrorCounter.inc()
  def commsEventNotFoundError(): Unit = commsEventNotFoundErrorCounter.inc()
  def commsEventBadRequestError(): Unit = commsEventBadRequestErrorCounter.inc()
  def commsEventUnexpectedError(): Unit = commsEventUnexpectedErrorCounter.inc()
  def commsEventQueuedForRetry(): Unit = commsEventQueuedForRetryCounter.inc()

  def emailMessageEnqueued(): Unit = emailMessageEnqueuedCounter.inc()
  def emailMessageDequeued(): Unit = emailMessageDequeuedCounter.inc()
  def emailMessageParsingError(): Unit = emailMessageParsingErrorCounter.inc()
  def emailMessageNotFoundError(): Unit = emailMessageNotFoundErrorCounter.inc()
  def emailMessageBadRequestError(): Unit = emailMessageBadRequestErrorCounter.inc()
  def emailMessageUnexpectedError(): Unit = emailMessageUnexpectedErrorCounter.inc()
  def emailMessageQueuedForRetry(): Unit = emailMessageQueuedForRetryCounter.inc()

  def secureMessageEnqueued(): Unit = secureMessageEnqueuedCounter.inc()
  def secureMessageDequeued(): Unit = secureMessageDequeuedCounter.inc()
  def secureMessageGenericQueueNoRetryError(): Unit = secureMessageGenericQueueNoRetryErrorCounter.inc()
  def secureMessageBadRequestError(): Unit = secureMessageBadRequestErrorCounter.inc()
  def secureMessageSpecificParsingError(): Unit = secureMessageSpecificParsingErrorCounter.inc()
  def secureMessageUnexpectedError(): Unit = secureMessageUnexpectedErrorCounter.inc()
  def secureMessageQueuedForRetry(): Unit = secureMessageQueuedForRetryCounter.inc()

}
