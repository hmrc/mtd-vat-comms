/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels.messageTypes

import models.secureMessageAlertModels.{BankDetailsModel, CustomerModel, PreferencesModel, TransactorModel}
import play.api.libs.json.{Json, OFormat}

case class RepaymentsBankAccountChangeModel(
                                             templateId: String,
                                             vrn: String,
                                             formBundleReference: String,
                                             businessName: String,
                                             transactorDetails: TransactorModel,
                                             customerDetails: CustomerModel,
                                             preferences: PreferencesModel,
                                             bankAccountDetails: BankDetailsModel
                                           )
  extends MessageModel(templateId, vrn, formBundleReference, businessName, transactorDetails, customerDetails, preferences) {
}

object RepaymentsBankAccountChangeModel {
  implicit val formats: OFormat[RepaymentsBankAccountChangeModel] = Json.format[RepaymentsBankAccountChangeModel]
}
