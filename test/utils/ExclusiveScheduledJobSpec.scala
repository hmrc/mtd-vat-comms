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

package utils

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.wordspec.AnyWordSpec

import java.util.concurrent.{CountDownLatch, TimeUnit}
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class ExclusiveScheduledJobSpec extends AnyWordSpec with Matchers with ScalaFutures {


  override implicit def patienceConfig: PatienceConfig =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  class SimpleJob extends ExclusiveScheduledJob {

    val start = new CountDownLatch(1)

    def continueExecution(): Unit = start.countDown()

    val executionCount = new AtomicInteger(0)

    def executions: Int = executionCount.get()

    override def executeInMutex(implicit ec: ExecutionContext): Future[Result] =
      Future {
        start.await(1, TimeUnit.MINUTES)
        Result(executionCount.incrementAndGet().toString)
      }

    override def name: String = "simpleJob"

    override def initialDelay: FiniteDuration = FiniteDuration(1, TimeUnit.MINUTES)

    override def interval: FiniteDuration = FiniteDuration(1, TimeUnit.MINUTES)
  }

  "ExclusiveScheduledJob" should {
    import scala.concurrent.ExecutionContext.Implicits.global

    "let the jobs run in sequence" in {
      val job = new SimpleJob
      job.continueExecution()
      job.execute.futureValue.message shouldBe "1"
      job.execute.futureValue.message shouldBe "2"
      job.toString() shouldBe "simpleJob after 1 minute every 1 minute"
    }

    "not allow jobs to run in parallel" in {
      val job = new SimpleJob

      val pausedExecution = job.execute
      pausedExecution.isCompleted shouldBe false
      job.execute.futureValue.message shouldBe "Skipping execution: job running"

      job.continueExecution()
      pausedExecution.futureValue.message shouldBe "1"
    }

    "should tolerate exceptions in execution" in {
      val job = new SimpleJob() {
        override def executeInMutex(implicit ec: ExecutionContext): Future[Result] = throw new RuntimeException
      }

      Try(job.execute.futureValue)

      job.isRunning.futureValue shouldBe false
    }
  }
}
