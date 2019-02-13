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

package metrics

import com.codahale.metrics.Counter
import com.kenshoo.play.metrics.Metrics
import javax.inject.Inject

class QueueMetrics @Inject()(metrics: Metrics) {

  val commsEventEnqueuedCounter: Counter = metrics.defaultRegistry.counter("commsEvent.enqueued")
  val commsEventDequeuedCounter: Counter = metrics.defaultRegistry.counter("commsEvent.dequeued")

  val emailMessageEnqueuedCounter: Counter = metrics.defaultRegistry.counter("emailMessage.enqueued")
  val emailMessageDequeuedCounter: Counter = metrics.defaultRegistry.counter("emailMessage.dequeued")

  val secureMessageEnqueuedCounter: Counter = metrics.defaultRegistry.counter("secureMessage.enqueued")
  val secureMessageDequeuedCounter: Counter = metrics.defaultRegistry.counter("secureMessage.dequeued")

  def commsEventEnqueued(): Unit = commsEventEnqueuedCounter.inc()
  def commsEventDequeued(): Unit = commsEventEnqueuedCounter.inc()

  def emailMessageEnqueued(): Unit = emailMessageEnqueuedCounter.inc()
  def emailMessageDequeued(): Unit = emailMessageEnqueuedCounter.inc()

  def secureMessageEnqueued(): Unit = secureMessageEnqueuedCounter.inc()
  def secureMessageDequeued(): Unit = secureMessageEnqueuedCounter.inc()
}
