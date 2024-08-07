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

package utils

import java.util

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object Execution {

  object Implicits {
    implicit def defaultExecutionContext: ExecutionContext = Execution.Trampoline
  }

  object Trampoline extends ExecutionContextExecutor {
    private val local = new ThreadLocal[AnyRef]
    private object Empty
    @tailrec
    private def executeScheduled(): Unit = local.get match {
      case Empty => ( /* Nothing to run */ )

      case next: Runnable =>
        local.set(Empty)
        next.run()
        executeScheduled()

      case arrayDeque: util.ArrayDeque[_] =>
        val runnables = arrayDeque.asInstanceOf[util.ArrayDeque[Runnable]]

        while (!runnables.isEmpty) {
          val runnable = runnables.removeFirst()
          runnable.run()
        }

      case illegal => throw new IllegalStateException(
        s"Unsupported trampoline ThreadLocal value: $illegal")
    }

    def execute(runnable: Runnable): Unit = {
      local.get match {
        case null => try {
          local.set(Empty)
          runnable.run()
          executeScheduled()
        } finally {

          local.set(null)
        }

        case Empty =>
          local.set(runnable)

        case next: Runnable =>
          val runnables = new util.ArrayDeque[Runnable](4)
          runnables.addLast(next)
          runnables.addLast(runnable)
          local.set(runnables)

        case arrayDeque: util.ArrayDeque[_] =>
          val runnables = arrayDeque.asInstanceOf[util.ArrayDeque[Runnable]]
          runnables.addLast(runnable)

        case illegal => throw new IllegalStateException(
          s"Unsupported trampoline ThreadLocal value: $illegal")
      }
    }
    def reportFailure(t: Throwable): Unit = t.printStackTrace()

  }
}
