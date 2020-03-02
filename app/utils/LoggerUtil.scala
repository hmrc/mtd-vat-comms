/*
 * Copyright 2020 HM Revenue & Customs
 *
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
      logWarn(s"${leftValue.code} => ${leftValue.body}")
    }
    content
  }

}

// $COVERAGE-ON$
