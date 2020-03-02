/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels.messageTypes

import models.secureMessageAlertModels.{AddressDetailsModel, CustomerModel, PreferencesModel, TransactorModel}
import play.api.libs.json.{Json, OFormat}

case class PPOBChangeModel(
                            templateId: String,
                            vrn: String,
                            formBundleReference: String,
                            businessName: String,
                            transactorDetails: TransactorModel,
                            customerDetails: CustomerModel,
                            preferences: PreferencesModel,
                            addressDetails: AddressDetailsModel
                          )
  extends MessageModel(templateId, vrn, formBundleReference, businessName, transactorDetails, customerDetails, preferences) {
}

object PPOBChangeModel {
  implicit val formats: OFormat[PPOBChangeModel] = Json.format[PPOBChangeModel]
}
