/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels.messageTypes

import models.secureMessageAlertModels._

class MessageModel(
                    templateId: String,
                    vrn: String,
                    formBundleReference: String,
                    businessName: String,
                    transactorDetails: TransactorModel,
                    customerDetails: CustomerModel,
                    preferences: PreferencesModel
                  ) {
  val getTemplateId: String = templateId
  val getVrn: String = vrn
  val getFormBundleReference: String = formBundleReference
  val getBusinessName: String = businessName
  val getTransactorDetails: TransactorModel = transactorDetails
  val getCustomerDetails: CustomerModel = customerDetails
  val getPreferences: PreferencesModel = preferences
}
