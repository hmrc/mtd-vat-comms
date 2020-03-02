/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package modules

import services.{CommsEventQueuePollingService, EmailMessageQueuePollingService, SecureMessageQueuePollingService}
import com.google.inject.AbstractModule

class SchedulerModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[CommsEventQueuePollingService]).asEagerSingleton()
    bind(classOf[EmailMessageQueuePollingService]).asEagerSingleton()
    bind(classOf[SecureMessageQueuePollingService]).asEagerSingleton()
  }
}
