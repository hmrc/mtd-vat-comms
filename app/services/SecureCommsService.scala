/*
 * Copyright 2019 HM Revenue & Customs
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

package services

import java.util.UUID
import common.Constants._
import common.Constants.MessageKeys._
import common.Constants.TemplateIdReadableNames._
import config.AppConfig
import connectors.SecureCommsServiceConnector
import javax.inject.Inject
import models.{ErrorModel, GenericQueueNoRetryError, SecureCommsMessageModel}
import models.secureCommsServiceModels.{SecureCommsServiceRequestModel, _}
import models.secureMessageAlertModels.messageTypes._
import models.viewModels.VatPPOBViewModel
import utils.SecureCommsMessageParser._
import utils.TemplateMappings._
import utils.Base64Encoding._
import views.html._
import play.api.i18n.{I18nSupport, MessagesApi}
import utils.DateFormatter._
import utils.LoggerUtil.logWarn

import scala.concurrent.{ExecutionContext, Future}

class SecureCommsService @Inject()(secureCommsServiceConnector: SecureCommsServiceConnector)
                                  (implicit val messagesApi: MessagesApi, appConfig: AppConfig) extends I18nSupport {

  def sendSecureCommsMessage(workItem: SecureCommsMessageModel)
                            (implicit ec: ExecutionContext): Future[Either[ErrorModel, Boolean]] = {

    parseModel(workItem) match {
      case Left(_) => Future(Left(GenericQueueNoRetryError))
      case Right(messageModel) =>
        getRequest(messageModel) match {
          case Left(_: ErrorModel) => Future(Left(GenericQueueNoRetryError))
          case Right(model: SecureCommsServiceRequestModel) =>
            secureCommsServiceConnector.sendMessage(model).map {
              case Right(_) => Right(true)
              case Left(error: ErrorModel) => Left(error)
            }
        }
    }

  }

  private def getRequest(messageModel: MessageModel): Either[ErrorModel, SecureCommsServiceRequestModel] = {

    val isTransactor = messageModel.getTransactorDetails.transactorEmail.nonEmpty

    isTemplateIdApproval(messageModel.getTemplateId) match {
      case None =>
        logWarn(content = s"[SecureCommsService][getRequest] - Unexpected Template Id encountered:  ${messageModel.getTemplateId}")
        Left(GenericQueueNoRetryError)
      case Some(isApproval) =>
        Right(buildResponse(messageModel, isTransactor, isApproval))
    }
  }

  private[services] def buildResponse(messageModel: MessageModel, isTransactor: Boolean,
                                      isApproval: Boolean): SecureCommsServiceRequestModel = {

    val vrn = messageModel.getVrn
    val businessName = messageModel.getBusinessName
    messageModel match {
      case deregModel: DeRegistrationModel =>
        val html = getDeregistrationChangeHtml(deregModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval, isTransactor)
        buildSecureCommsServiceRequestModel(
          html, deregModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        )
      case ppobModel: PPOBChangeModel =>
        val html = getPpobChangeHtml(ppobModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = PPOB_BASE_KEY, isApproval, isTransactor)
        buildSecureCommsServiceRequestModel(
          html, ppobModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        )
      case repaymentModel: RepaymentsBankAccountChangeModel =>
        val html = getBankDetailsChangeHtml(repaymentModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval, isTransactor)
        buildSecureCommsServiceRequestModel(
          html, repaymentModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        )
      case staggerModel: VATStaggerChangeModel =>
        val html = getStaggerChangeHtml(staggerModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = STAGGER_BASE_KEY, isApproval, isTransactor)
        buildSecureCommsServiceRequestModel(
          html, staggerModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        )
      case emailModel: EmailAddressChangeModel =>
        val html = getEmailChangeHtml(emailModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = EMAIL_BASE_KEY, isApproval, isTransactor)
        buildSecureCommsServiceRequestModel(
          html, emailModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        )
    }
  }

  private def buildSecureCommsServiceRequestModel(htmlContent: String,
                                                  userEmail: String,
                                                  subject: String,
                                                  vrn: String,
                                                  salutation: String,
                                                  isTransactor: Boolean): SecureCommsServiceRequestModel = {

    val externalRefModel = ExternalRefModel(id = UUID.randomUUID().toString, source = SecureCommsServiceFieldValues.MTDP)
    val taxIdentifierModel = TaxIdentifierModel(name = TAX_IDENTIFIER_MTDVAT, value = vrn)
    val nameModel = NameModel(line1 = salutation)
    val recipientModel = RecipientModel(taxIdentifier = taxIdentifierModel, name = nameModel, email = userEmail)
    val templateId = if(isTransactor) CLIENT_NOTIFICATION_AGENT_CHANGE else CLIENT_NOTIFICATION_SELF_CHANGE
    SecureCommsServiceRequestModel(
      externalRefModel, recipientModel, templateId, subject, encode(htmlContent)
    )
  }

  private def getEmailChangeHtml(emailAddressChangeModel: EmailAddressChangeModel,
                                 isApproval: Boolean): String = {
    if (isApproval) {
      vatEmailApproved(emailAddressChangeModel.customerDetails.customerEmail).toString
    } else {
      vatEmailRejected().toString
    }
  }

  private def getBankDetailsChangeHtml(repaymentsBankAccountChangeModel: RepaymentsBankAccountChangeModel,
                                       isApproval: Boolean): String = {
    if (isApproval) {
      vatBankDetailsApproved(repaymentsBankAccountChangeModel.bankAccountDetails.bankAccountName,
        repaymentsBankAccountChangeModel.bankAccountDetails.bankSortCode,
        repaymentsBankAccountChangeModel.bankAccountDetails.bankAccountNumber).toString
    } else {
      vatBankDetailsRejected(repaymentsBankAccountChangeModel.bankAccountDetails.bankAccountName,
        repaymentsBankAccountChangeModel.businessName).toString
    }
  }

  private def getDeregistrationChangeHtml(deRegistrationModel: DeRegistrationModel,
                                          isApproval: Boolean): String = {
    if (isApproval) {
      vatDeregApproved(etmpToFullMonthDateString(deRegistrationModel.effectiveDateOfDeRegistration)).toString
    } else {
      vatDeregRejected().toString
    }
  }

  private def getPpobChangeHtml(ppobChangeModel: PPOBChangeModel,
                                isApproval: Boolean): String = {
    if (isApproval) {
      vatPPOBApproved(
        VatPPOBViewModel(
          ppobChangeModel.addressDetails.addressLine1,
          ppobChangeModel.addressDetails.addressLine2,
          if (ppobChangeModel.addressDetails.addressLine3.isEmpty) None else Some(ppobChangeModel.addressDetails.addressLine3),
          if (ppobChangeModel.addressDetails.addressLine4.isEmpty) None else Some(ppobChangeModel.addressDetails.addressLine4),
          if (ppobChangeModel.addressDetails.addressLine5.isEmpty) None else Some(ppobChangeModel.addressDetails.addressLine5),
          if (ppobChangeModel.addressDetails.postCode.isEmpty) None else Some(ppobChangeModel.addressDetails.postCode),
          if (ppobChangeModel.addressDetails.countryName.isEmpty) None else Some(ppobChangeModel.addressDetails.countryName)
        )
      ).toString
    } else {
      vatPPOBRejected().toString
    }
  }

  private def getStaggerChangeHtml(vatStaggerChangeModel: VATStaggerChangeModel,
                                   isApproval: Boolean): String = {
    if (isApproval) {
      vatStaggerApproved(vatStaggerChangeModel.stagger.toUpperCase).toString
    } else {
      vatStaggerRejected().toString
    }
  }

  private[services] def getSubjectForBaseKey(baseSubjectKey: String, isApproval: Boolean, isTransactor :Boolean): String = {
    val statusKey = if(isApproval) baseSubjectKey.concat(APPROVED_SUFFIX) else baseSubjectKey.concat(REJECTED_SUFFIX)
    if(isTransactor) messagesApi(statusKey.concat(TITLE_KEY_TRANSACTOR)) else messagesApi(statusKey.concat(TITLE_KEY_CLIENT))
  }

}
