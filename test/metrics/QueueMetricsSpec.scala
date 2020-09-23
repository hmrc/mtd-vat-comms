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

import base.BaseSpec
import com.codahale.metrics.{Counter, MetricRegistry}
import com.kenshoo.play.metrics.Metrics
import org.scalatestplus.mockito.MockitoSugar


class QueueMetricsSpec extends BaseSpec with MockitoSugar {
  "QueueMetrics" when {

    "the registry counters are incremented" should {

      "increment the commsEvent.enqueued count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("commsEvent.enqueued").getCount

        queueMetrics.commsEventEnqueued()

        mockedRegistry.counter("commsEvent.enqueued").getCount shouldBe countBefore + 1

      }

      "increment the commsEvent.dequeued count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("commsEvent.dequeued").getCount

        queueMetrics.commsEventDequeued()

        mockedRegistry.counter("commsEvent.dequeued").getCount shouldBe countBefore + 1

      }

      "increment the commsEvent.genericParsingError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("commsEvent.genericParsingError").getCount

        queueMetrics.commsEventGenericParsingError()

        mockedRegistry.counter("commsEvent.genericParsingError").getCount shouldBe countBefore + 1

      }

      "increment the commsEvent.jsonParsingError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("commsEvent.jsonParsingError").getCount

        queueMetrics.commsEventJsonParsingError()

        mockedRegistry.counter("commsEvent.jsonParsingError").getCount shouldBe countBefore + 1

      }

      "increment the commsEvent.notFoundError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("commsEvent.notFoundError").getCount

        queueMetrics.commsEventNotFoundError()

        mockedRegistry.counter("commsEvent.notFoundError").getCount shouldBe countBefore + 1

      }

      "increment the commsEvent.badRequestError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("commsEvent.badRequestError").getCount

        queueMetrics.commsEventBadRequestError()

        mockedRegistry.counter("commsEvent.badRequestError").getCount shouldBe countBefore + 1

      }

      "increment the commsEvent.unexpectedError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("commsEvent.unexpectedError").getCount

        queueMetrics.commsEventUnexpectedError()

        mockedRegistry.counter("commsEvent.unexpectedError").getCount shouldBe countBefore + 1

      }

      "increment the commsEvent.queuedForRetry count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("commsEvent.queuedForRetry").getCount

        queueMetrics.commsEventQueuedForRetry()

        mockedRegistry.counter("commsEvent.queuedForRetry").getCount shouldBe countBefore + 1

      }

      "increment the emailMessage.parsingError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("emailMessage.parsingErro").getCount

        queueMetrics.emailMessageParsingError()

        mockedRegistry.counter("emailMessage.parsingError").getCount shouldBe countBefore + 1

      }

      "increment the emailMessage.enqueued count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("emailMessage.enqueued").getCount

        queueMetrics.emailMessageEnqueued()

        mockedRegistry.counter("emailMessage.enqueued").getCount shouldBe countBefore + 1
      }

      "increment the emailMessage.dequeued count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("emailMessage.dequeued").getCount

        queueMetrics.emailMessageDequeued()

        mockedRegistry.counter("emailMessage.dequeued").getCount shouldBe countBefore + 1
      }

      "increment the emailMessage.notFoundError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("emailMessage.notFoundError").getCount

        queueMetrics.emailMessageNotFoundError()

        mockedRegistry.counter("emailMessage.notFoundError").getCount shouldBe countBefore + 1
      }

      "increment the emailMessage.badRequestError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("emailMessage.badRequestError").getCount

        queueMetrics.emailMessageBadRequestError()

        mockedRegistry.counter("emailMessage.badRequestError").getCount shouldBe countBefore + 1
      }

      "increment the emailMessage.unexpectedError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("emailMessage.unexpectedError").getCount

        queueMetrics.emailMessageUnexpectedError()

        mockedRegistry.counter("emailMessage.unexpectedError").getCount shouldBe countBefore + 1
      }

      "increment the emailMessage.queuedForRetry count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("emailMessage.queuedForRetry").getCount

        queueMetrics.emailMessageQueuedForRetry()

        mockedRegistry.counter("emailMessage.queuedForRetry").getCount shouldBe countBefore + 1
      }

      "increment the secureMessage.enqueued count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("secureMessage.enqueued").getCount

        queueMetrics.secureMessageEnqueued()

        mockedRegistry.counter("secureMessage.enqueued").getCount shouldBe countBefore + 1
      }


      "increment the secureMessage.dequeued count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("secureMessage.dequeued").getCount

        queueMetrics.secureMessageDequeued()

        mockedRegistry.counter("secureMessage.dequeued").getCount shouldBe countBefore + 1
      }

      "increment the secureMessage.genericQueueNoRetryError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("secureMessage.genericQueueNoRetryError").getCount

        queueMetrics.secureMessageGenericQueueNoRetryError()

        mockedRegistry.counter("secureMessage.genericQueueNoRetryError").getCount shouldBe countBefore + 1
      }

      "increment the secureMessage.badRequestError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("secureMessage.badRequestError").getCount

        queueMetrics.secureMessageBadRequestError()

        mockedRegistry.counter("secureMessage.badRequestError").getCount shouldBe countBefore + 1
      }

      "increment the secureMessage.specificParsingError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("secureMessage.specificParsingError").getCount

        queueMetrics.secureMessageSpecificParsingError()

        mockedRegistry.counter("secureMessage.specificParsingError").getCount shouldBe countBefore + 1
      }

      "increment the secureMessage.unexpectedError count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("secureMessage.unexpectedError").getCount

        queueMetrics.secureMessageUnexpectedError()

        mockedRegistry.counter("secureMessage.unexpectedError").getCount shouldBe countBefore + 1
      }

      "increment the secureMessage.queuedForRetry count" in new Setup {

        val countBefore: Long = mockedRegistry.counter("secureMessage.queuedForRetry").getCount

        queueMetrics.secureMessageQueuedForRetry()

        mockedRegistry.counter("secureMessage.queuedForRetry").getCount shouldBe countBefore + 1
      }

    }

  }
}


  // ScalaMock doesn't currently handle classes.
  private trait Setup {

    val mockedMetrics: Metrics = new MockMetrics
    val mockedRegistry: MetricRegistry = new MetricRegistry()
    val queueMetrics = new QueueMetrics(mockedMetrics)

    private class MockMetrics extends Metrics {
      override def defaultRegistry: MetricRegistry = mockedRegistry

      override def toJson: String = ???
    }

  }
