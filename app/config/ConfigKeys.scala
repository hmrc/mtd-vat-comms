/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package config

object ConfigKeys {

  val desBase: String = "des"
  val desAuthorisationToken = "microservice.services.des.authorisationToken"
  val desEnvironment = "microservice.services.des.environment"

  val failureRetryAfterProperty: String = "queue.retryAfter"
  val queuePollingInterval: String = "queue.pollingInterval"
  val queueInitialWait: String = "queue.initialWait"
  val queueItemExpiry: String = "queue.expiryInSeconds"
  val queueToggleProperty: String = "queue.toggle"

  val emailServiceBase = "emailService"

  val secureCommsServiceBase = "secureCommsService"

  val tribunalUrl: String = "govuk.tribunalUrl"

  val manageVatSubscriptionHost = "manage-vat-subscription-frontend.host"
  val manageVatSubscriptionUri: String = "manage-vat-subscription-frontend.changeBusinessDetailsUri"

  val vatSummaryHost: String = "vat-summary-frontend.host"
  val vatSummaryUri: String = "vat-summary-frontend.vatOverviewUri"

  val vatSignUpHost: String = "vat-sign-up-frontend.host"
  val reSignUpUri: String = "vat-sign-up-frontend.reSignUpUri"
}
