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

package utils

import models.ErrorModel
import play.api.Logger

// $COVERAGE-OFF$

object LoggerUtil {

  def logInfo(content: String): Unit = Logger.info(content)
  def logDebug(content: String): Unit = Logger.debug(content)
  def logWarn(content: String): Unit = Logger.warn(content)
  def logWarn(content: String, throwable: Throwable): Unit = Logger.warn(content, throwable)
  def logError(content: String): Unit = Logger.error(content)
  def logError(content: String, throwable: Throwable): Unit = Logger.error(content, throwable)

  def logWarnEitherError[T](content: Either[ErrorModel, T]): Either[ErrorModel, T] = {
    if(content.isLeft) {
      val leftValue = content.left.get
      logWarn(s"[LoggerUtil][logWarnEitherError] ${leftValue.code}: ${leftValue.body}")
    }
    content
  }

}

// $COVERAGE-ON$
