/*
 * Copyright 2020 HM Revenue & Customs
 *
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
  val secureMessageNotFoundMissingTaxpayerErrorCounter: Counter = metrics.defaultRegistry.counter("secureMessage.notFoundMissingTaxpayerError")
  val secureMessageNotFoundUnverifiedEmailErrorCounter: Counter = metrics.defaultRegistry.counter("secureMessage.notFoundUnverifiedEmailError")
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
  def secureMessageNotFoundMissingTaxpayerError(): Unit = secureMessageNotFoundMissingTaxpayerErrorCounter.inc()
  def secureMessageNotFoundUnverifiedEmailError(): Unit = secureMessageNotFoundUnverifiedEmailErrorCounter.inc()
  def secureMessageBadRequestError(): Unit = secureMessageBadRequestErrorCounter.inc()
  def secureMessageSpecificParsingError(): Unit = secureMessageSpecificParsingErrorCounter.inc()
  def secureMessageUnexpectedError(): Unit = secureMessageUnexpectedErrorCounter.inc()
  def secureMessageQueuedForRetry(): Unit = secureMessageQueuedForRetryCounter.inc()

}
