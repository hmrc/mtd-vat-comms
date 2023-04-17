/*
 * Copyright 2023 HM Revenue & Customs
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

import common.Constants.MessageKeys._
import common.Constants.TemplateIdReadableNames._
import common.Constants._
import config.AppConfig
import connectors.SecureCommsServiceConnector
import models.secureCommsServiceModels.{SecureCommsServiceRequestModel, _}
import models.secureMessageAlertModels.messageTypes._
import models.viewModels.VatPPOBViewModel
import models.{ErrorModel, GenericQueueNoRetryError, SecureCommsMessageModel, SpecificParsingError}
import play.api.i18n._
import utils.Base64Encoding._
import utils.DateFormatter._
import utils.SecureCommsMessageParser._
import utils.TemplateMappings._
import views.html._

import java.time.format.DateTimeParseException
import java.util.Locale.ENGLISH
import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SecureCommsService @Inject()(secureCommsServiceConnector: SecureCommsServiceConnector, vatEmailApproved: VatEmailApproved,
                                   vatEmailRejected: VatEmailRejected, vatBankDetailsApproved: VatBankDetailsApproved,
                                   vatBankDetailsRejected: VatBankDetailsRejected, vatDeregApproved: VatDeregApproved,
                                   vatDeregRejected: VatDeregRejected, vatPPOBApproved: VatPPOBApproved,
                                   vatPPOBRejected: VatPPOBRejected, vatStaggerApproved: VatStaggerApproved,
                                   vatStaggerRejected: VatStaggerRejected,
                                   vatStaggerApprovedLeaveAnnualAccounting: VatStaggerApprovedLeaveAnnualAccounting,
                                   vatContactNumbersApproved: VatContactNumbersApproved, vatContactNumbersRejected: VatContactNumbersRejected,
                                   vatWebsiteApproved: VatWebsiteApproved, vatWebsiteRejected: VatWebsiteRejected)
                                  (implicit val appConfig: AppConfig, val messagesApi: MessagesApi) extends I18nSupport {

  val lang: Lang = new Lang(ENGLISH)

  implicit val messages: Messages = MessagesImpl(lang, messagesApi)

  def sendSecureCommsMessage(workItem: SecureCommsMessageModel)
                            (implicit ec: ExecutionContext): Future[Either[ErrorModel, Boolean]] =

    parseModel(workItem) match {
      case Left(_) => Future(Left(SpecificParsingError))
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

  private def getRequest(messageModel: MessageModel): Either[ErrorModel, SecureCommsServiceRequestModel] = {

    val isTransactor = messageModel.templateId.endsWith("C")

    isTemplateIdApproval(messageModel.templateId) match {
      case None =>
        logger.warn(s"[SecureCommsService][getRequest] - Unexpected Template Id encountered:  ${messageModel.templateId}")
        Left(GenericQueueNoRetryError)
      case Some(isApproval) =>
        buildResponse(messageModel, isTransactor, isApproval)
    }
  }

  //scalastyle:off method.length
  private[services] def buildResponse(messageModel: MessageModel, isTransactor: Boolean,
                                      isApproval: Boolean): Either[ErrorModel, SecureCommsServiceRequestModel] = {

    val vrn = messageModel.vrn
    val businessName = messageModel.businessName
    messageModel match {
      case deregModel: DeRegistrationModel =>
        val html = getDeregistrationChangeHtml(deregModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = DEREG_BASE_KEY, isApproval, isTransactor)
        Right(buildSecureCommsServiceRequestModel(
          html, deregModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        ))
      case ppobModel: PPOBChangeModel =>
        val html = getPpobChangeHtml(ppobModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = PPOB_BASE_KEY, isApproval, isTransactor)
        Right(buildSecureCommsServiceRequestModel(
          html, ppobModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        ))
      case repaymentModel: RepaymentsBankAccountChangeModel =>
        val html = getBankDetailsChangeHtml(repaymentModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = BANK_DETAILS_BASE_KEY, isApproval, isTransactor)
        Right(buildSecureCommsServiceRequestModel(
          html, repaymentModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        ))
      case staggerModel: VATStaggerChangeModel if staggerModel.staggerDetails.stagger.trim.isEmpty =>
        logger.warn("[SecureCommsService][buildResponse] - Blank stagger code received, queueing permanent failure.")
        Left(GenericQueueNoRetryError)
      case staggerModel: VATStaggerChangeModel =>
        val html = getStaggerChangeHtml(staggerModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = STAGGER_BASE_KEY, isApproval, isTransactor)
        Right(buildSecureCommsServiceRequestModel(
          html, staggerModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        ))
      case emailModel: EmailAddressChangeModel =>
        val html = getEmailChangeHtml(emailModel, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = EMAIL_BASE_KEY, isApproval, isTransactor)
        Right(buildSecureCommsServiceRequestModel(
          html, emailModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        ))
      case websiteModel: WebAddressChangeModel =>
        val isRemoval: Boolean = websiteModel.websiteAddress.isEmpty
        val webAddressOpt: Option[String] = Option(websiteModel.websiteAddress).filter(_.nonEmpty)
        val html = getWebAddressChangeHtml(isTransactor, webAddressOpt, isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = WEBSITE_BASE_KEY, isApproval, isTransactor, isRemoval)
        Right(buildSecureCommsServiceRequestModel(html, websiteModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        ))
      case contactNumbersModel: ContactNumbersChangeModel =>
        val html = getContactNumbersChangeHtml(isApproval)
        val subject = getSubjectForBaseKey(baseSubjectKey = CONTACT_NUMBERS_BASE_KEY, isApproval, isTransactor)
        Right(buildSecureCommsServiceRequestModel(
          html, contactNumbersModel.customerDetails.customerEmail, subject, vrn, businessName, isTransactor
        ))
    }
  }

  private def buildSecureCommsServiceRequestModel(htmlContent: String,
                                                  userEmail: String,
                                                  subject: String,
                                                  vrn: String,
                                                  salutation: String,
                                                  isTransactor: Boolean): SecureCommsServiceRequestModel = {

    val externalRefModel = ExternalRefModel(id = UUID.randomUUID().toString, source = SecureCommsServiceFieldValues.MDTP)
    val taxIdentifierModel = TaxIdentifierModel(name = TAX_IDENTIFIER_MTDVAT, value = vrn)
    val nameModel = NameModel(line1 = salutation)
    val recipientModel = RecipientModel(taxIdentifier = taxIdentifierModel, name = nameModel, email = userEmail)
    val templateId = if (isTransactor) CLIENT_NOTIFICATION_AGENT_CHANGE else CLIENT_NOTIFICATION_SELF_CHANGE

    SecureCommsServiceRequestModel(
      externalRefModel, recipientModel, templateId, subject, encode(htmlContent)
    )
  }

  private def getEmailChangeHtml(emailAddressChangeModel: EmailAddressChangeModel,
                                 isApproval: Boolean): String =
    if (isApproval) {
      vatEmailApproved(emailAddressChangeModel.customerDetails.customerEmail).toString
    } else {
      vatEmailRejected().toString
    }

  private def getBankDetailsChangeHtml(repaymentsBankAccountChangeModel: RepaymentsBankAccountChangeModel,
                                       isApproval: Boolean): String =
    if (isApproval) {
      vatBankDetailsApproved(repaymentsBankAccountChangeModel.bankAccountDetails.bankAccountName,
        repaymentsBankAccountChangeModel.bankAccountDetails.bankSortCode,
        repaymentsBankAccountChangeModel.bankAccountDetails.bankAccountNumber).toString
    } else {
      vatBankDetailsRejected(repaymentsBankAccountChangeModel.bankAccountDetails.bankAccountName,
        repaymentsBankAccountChangeModel.businessName).toString
    }

  private def getDeregistrationChangeHtml(deRegistrationModel: DeRegistrationModel,
                                          isApproval: Boolean): String =
    if (isApproval) {
      vatDeregApproved(etmpToFullMonthDateString(deRegistrationModel.effectiveDateOfDeregistration)).toString
    } else {
      vatDeregRejected().toString
    }

  private def getPpobChangeHtml(ppobChangeModel: PPOBChangeModel,
                                isApproval: Boolean): String =
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
      vatPPOBRejected(isTransactor = false).toString
    }

  private val annualAccountLeaveStaggerCodes = List("YA", "YB", "YC", "YD", "YE", "YF", "YG", "YH", "YI", "YJ", "YK", "YL")

  private def getStaggerChangeHtml(vatStaggerChangeModel: VATStaggerChangeModel,
                                   isApproval: Boolean): String =
    if (isApproval) {
      try {
        if (annualAccountLeaveStaggerCodes.contains(vatStaggerChangeModel.staggerDetails.previousStagger)) {
          vatStaggerApprovedLeaveAnnualAccounting(
            vatStaggerChangeModel.staggerDetails.stagger,
            vatStaggerChangeModel.staggerDetails.newStaggerStartDate,
            vatStaggerChangeModel.staggerDetails.newStaggerPeriodEndDate,
            vatStaggerChangeModel.staggerDetails.previousStaggerEndDate
          ).toString()
        } else {
          vatStaggerApproved(vatStaggerChangeModel.staggerDetails.stagger.toUpperCase).toString
        }
      } catch {
        case exception: DateTimeParseException =>
          logger.warn(s"[SecureCommsService][getStaggerChangeHtml] - Error parsing one of the provided dates.\n" +
            s"New stagger start date: ${vatStaggerChangeModel.staggerDetails.newStaggerStartDate}\n" +
            s"New stagger end date: ${vatStaggerChangeModel.staggerDetails.newStaggerPeriodEndDate}\n" +
            s"Prev stagger end date: ${vatStaggerChangeModel.staggerDetails.previousStaggerEndDate}")
          throw exception
        case exception: MatchError =>
          logger.warn("[SecureCommsService][getStaggerChangeHtml] - " +
            s"Unrecognised stagger code: ${vatStaggerChangeModel.staggerDetails.stagger.toUpperCase}")
          throw exception
      }
    } else {
      vatStaggerRejected().toString
    }

  private def getWebAddressChangeHtml(isTransactor: Boolean, websiteAddress: Option[String],
                                      isApproval: Boolean): String =
    if (isApproval) {
      vatWebsiteApproved(isTransactor, websiteAddress).toString
    } else {
      vatWebsiteRejected(websiteAddress.isEmpty).toString()
    }

  private def getContactNumbersChangeHtml(isApproval: Boolean): String =
    if (isApproval) {
      vatContactNumbersApproved().toString
    } else {
      vatContactNumbersRejected().toString()
    }

  private[services] def getSubjectForBaseKey(baseSubjectKey: String, isApproval: Boolean,
                                             isTransactor: Boolean, isRemoval: Boolean = false): String = {

    val statusKey = if (isApproval) baseSubjectKey.concat(APPROVED_SUFFIX) else baseSubjectKey.concat(REJECTED_SUFFIX)
    val transactorSubmitted = if (isTransactor) {
      statusKey.concat(TITLE_KEY_TRANSACTOR)
    } else {
      statusKey.concat(TITLE_KEY_CLIENT)
    }

    (baseSubjectKey, isRemoval) match {
      case (WEBSITE_BASE_KEY, true) =>  messages(transactorSubmitted.concat(REMOVE_SUFFIX))
      case (WEBSITE_BASE_KEY, false) => messages(transactorSubmitted.concat(CHANGE_SUFFIX))
      case _ => messages(transactorSubmitted)
    }
  }
}
