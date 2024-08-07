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

import utils.Iteratee.Internal.executeFuture

import scala.concurrent.{ExecutionContext, Future}
import utils.Execution.Implicits.{defaultExecutionContext => dec}

trait Enumerator[E] {
  parent =>
  def apply[A](i: Iteratee[E, A]): Future[Iteratee[E, A]]
  def |>>>[A](i: Iteratee[E, A]): Future[A] = apply(i).flatMap(_.run)(dec)
  def run[A](i: Iteratee[E, A]): Future[A] = |>>>(i)
}

object Enumerator {
  def generateM[E](e: => Future[Option[E]])(implicit ec: ExecutionContext): Enumerator[E] = checkContinue0(new TreatCont0[E] {
    def apply[A](loop: Iteratee[E, A] => Future[Iteratee[E, A]], k: Input[E] => Iteratee[E, A]): Future[Iteratee[E, A]] =
      executeFuture(e)(ec).flatMap {
        case Some(e) => loop(k(Input.El(e)))
        case None => Future.successful(Cont(k))
      }(ec)
    })(ec)

  trait TreatCont0[E] {
    def apply[A](loop: Iteratee[E, A] => Future[Iteratee[E, A]], k: Input[E] => Iteratee[E, A]): Future[Iteratee[E, A]]
  }

  def checkContinue0[E](inner: TreatCont0[E])(implicit ec: ExecutionContext): Enumerator[E] = new Enumerator[E] {
    def apply[A](it: Iteratee[E, A]): Future[Iteratee[E, A]] = {
      def step(it: Iteratee[E, A]): Future[Iteratee[E, A]] = it.fold {
        case Step.Done(a, e) => Future.successful(Done(a, e))
        case Step.Cont(k) => inner[A](step, k)
        case Step.Error(msg, e) => Future.successful(Error(msg, e))
      }(ec)
      step(it)
    }
  }

}
