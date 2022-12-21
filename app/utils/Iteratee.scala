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

package utils

import utils.Iteratee.Internal.{eagerFuture, executeFuture}
import utils.Execution.Implicits.{defaultExecutionContext => dec}

import scala.concurrent.{ExecutionContext, Future}

object Iteratee {

  object Internal {
    import scala.util.control.NonFatal
    val identityFunc: (Any => Any) = (x: Any) => x
    def eagerFuture[A](body: => A): Future[A] =
      try Future.successful(body) catch {
        case NonFatal(e) => Future.failed(e)
      }
    def executeFuture[A](body: => Future[A])(implicit ec: ExecutionContext): Future[A] = {
      Future {
        body
      }(ec).flatMap(identityFunc.asInstanceOf[Future[A] => Future[A]])(Execution.Trampoline)
    }
  }

  def flatten[E, A](i: Future[Iteratee[E, A]]): Iteratee[E, A] =
    new FutureIteratee[E, A](i)
  def fold[E, A](state: A)(f: (A, E) => A)(implicit ec: ExecutionContext): Iteratee[E, A] =
    foldM(state)((a, e: E) => eagerFuture(f(a, e)))(ec)
  def foldM[E, A](state: A)(f: (A, E) => Future[A])(implicit ec: ExecutionContext): Iteratee[E, A] = {
    def step(s: A)(i: Input[E]): Iteratee[E, A] = i match {
      case Input.EOF => Done(s, Input.EOF)
      case Input.Empty => Cont[E, A](step(s))
      case Input.El(e) => val newS = Internal.executeFuture(f(s, e))(ec); flatten(newS.map(s1 => Cont[E, A](step(s1)))(ec))
    }
    Cont[E, A](step(state))
  }
  def fold2[E, A](state: A)(f: (A, E) => Future[(A, Boolean)])(implicit ec: ExecutionContext): Iteratee[E, A] = {
    def step(s: A)(i: Input[E]): Iteratee[E, A] = i match {

      case Input.EOF => Done(s, Input.EOF)
      case Input.Empty => Cont[E, A](step(s))
      case Input.El(e) => { val newS = executeFuture(f(s, e))(ec); flatten(newS.map[Iteratee[E, A]] { case (s1, done) =>
        if (!done) Cont[E, A](step(s1)) else Done(s1, Input.Empty) }(dec)) }
    }
    Cont[E, A](step(state))
  }
  def getChunks[E](implicit ec: ExecutionContext): Iteratee[E, List[E]] =
    fold[E, List[E]](Nil) { (els, chunk) => chunk +: els }(ec).map(_.reverse)(ec)
}

private final class FutureIteratee[E, A](itFut: Future[Iteratee[E, A]]) extends Iteratee[E, A] {
  def fold[B](folder: Step[E, A] => Future[B])(implicit ec: ExecutionContext): Future[B] =
    itFut.flatMap { _.fold(folder)(ec) }(ec)
}

trait Iteratee[E, +A] {
  self =>

  def run(implicit ec: ExecutionContext): Future[A] = fold({
    case Step.Done(a, _) => Future.successful(a)
    case Step.Cont(k) => k(Input.EOF).fold({
      case Step.Done(a1, _) => Future.successful(a1)
      case Step.Cont(_) => sys.error("diverging iteratee after Input.EOF")
      case Step.Error(msg, e) => sys.error(msg)
    })(ec)
    case Step.Error(msg, e) => sys.error(msg)
  })(ec)
  def pureFoldNoEC[B](folder: Step[E, A] => B)(implicit ec: ExecutionContext): Future[B] =
    pureFold(folder)(ec)
  def executeIteratee[G, F](body: => Iteratee[G, F])(implicit ec: ExecutionContext): Iteratee[G, F] = Iteratee.flatten(Future(body)(ec))
  def pureFold[B](folder: Step[E, A] => B)(implicit ec: ExecutionContext): Future[B] = fold(s => eagerFuture(folder(s)))(ec)
  def pureFlatFold[B, C](folder: Step[E, A] => Iteratee[B, C])(implicit ec: ExecutionContext): Iteratee[B, C] = Iteratee.flatten(pureFold(folder)(ec))

  def flatMap[B](f: A => Iteratee[E, B]): Iteratee[E, B] = self.pureFlatFold {
    case Step.Done(a, Input.Empty) => executeIteratee(f(a))(dec)
    case Step.Done(a, e) => executeIteratee(f(a))(dec).pureFlatFold {
      case Step.Done(a, _) => Done(a, e)
      case Step.Cont(k) => k(e)
      case Step.Error(msg, e) => Error(msg, e)
    }(dec)
    case Step.Cont(k) => Cont((in: Input[E]) => executeIteratee(k(in))(dec).flatMap(f))
    case Step.Error(msg, e) => Error(msg, e)
  }
  def flatMapM[B](f: A => Future[Iteratee[E, B]])(implicit ec: ExecutionContext): Iteratee[E, B] = self.flatMap(a => Iteratee.flatten(f(a)))

  def mapM[B](f: A => Future[B])(implicit ec: ExecutionContext): Iteratee[E, B] = self.flatMapM(a => f(a).map[Iteratee[E, B]](b => Done(b))(ec))(ec)

  def fold[B](folder: Step[E, A] => Future[B])(implicit ec: ExecutionContext): Future[B]

  def map[B](f: A => B)(implicit ec: ExecutionContext): Iteratee[E, B] = this.flatMap(a => Done(f(a), Input.Empty))

}

sealed trait Input[+E] {
  def map[U](f: (E => U)): Input[U] = this match {
    case Input.El(e) => Input.El(f(e))
    case Input.Empty => Input.Empty
    case Input.EOF => Input.EOF
  }
}

object Input {
  case class El[+E](e: E) extends Input[E]
  case object Empty extends Input[Nothing]
  case object EOF extends Input[Nothing]
}

private final class ContIteratee[E, A](k: Input[E] => Iteratee[E, A]) extends Step.Cont[E, A](k) with StepIteratee[E, A] {
}

object Done {
  def apply[E, A](a: A, e: Input[E] = Input.Empty): Iteratee[E, A] = new DoneIteratee[E, A](a, e)
}

object Cont {
  def apply[E, A](k: Input[E] => Iteratee[E, A]): Iteratee[E, A] = new ContIteratee[E, A](k)
}

private final class DoneIteratee[E, A](a: A, e: Input[E]) extends Step.Done[A, E](a, e) with StepIteratee[E, A] {
  override def mapM[B](f: A => Future[B])(implicit ec: ExecutionContext): Iteratee[E, B] = {
    Iteratee.flatten(executeFuture {
      f(a).map[Iteratee[E, B]](Done(_, e))(ec)
    }(ec))
  }
}

private sealed trait StepIteratee[E, A] extends Iteratee[E, A] with Step[E, A] {
  final def immediateUnflatten: Step[E, A] = this
  final override def it: Iteratee[E, A] = this
  final override def fold[B](folder: Step[E, A] => Future[B])(implicit ec: ExecutionContext): Future[B] = {
    executeFuture {
      folder(immediateUnflatten)
    }(ec /* executeFuture handles preparation */ )
  }
}

private final class ErrorIteratee[E](msg: String, e: Input[E]) extends Step.Error[E](msg, e) with StepIteratee[E, Nothing] {
}

sealed trait Step[E, +A] {
  def it: Iteratee[E, A] = this match {
    case Step.Done(a, e) => Done(a, e)
    case Step.Cont(k) => Cont(k)
    case Step.Error(msg, e) => Error(msg, e)
  }
}

object Step {
  case class Done[+A, E](a: A, remaining: Input[E]) extends Step[E, A]
  case class Cont[E, +A](k: Input[E] => Iteratee[E, A]) extends Step[E, A]
  case class Error[E](msg: String, input: Input[E]) extends Step[E, Nothing]
}

object Error {
  def apply[E](msg: String, e: Input[E]): Iteratee[E, Nothing] = new ErrorIteratee[E](msg, e)
}
