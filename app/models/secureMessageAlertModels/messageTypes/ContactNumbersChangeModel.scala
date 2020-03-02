/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels.messageTypes

import models.secureMessageAlertModels.{ContactNumbersModel, CustomerModel, PreferencesModel, TransactorModel}
import play.api.libs.json.{Json, OFormat}

case class ContactNumbersChangeModel(
                                  templateId: String,
                                  vrn: String,
                                  formBundleReference: String,
                                  businessName: String,
                                  transactorDetails: TransactorModel,
                                  customerDetails: CustomerModel,
                                  preferences: PreferencesModel,
                                  contactNumbers: ContactNumbersModel
                                )
  extends MessageModel(templateId, vrn, formBundleReference, businessName, transactorDetails, customerDetails, preferences) {
}

object ContactNumbersChangeModel {
  implicit val formats: OFormat[ContactNumbersChangeModel] = Json.format[ContactNumbersChangeModel]
}
