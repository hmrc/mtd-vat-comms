/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels.messageTypes

import models.secureMessageAlertModels.{CustomerModel, PreferencesModel, TransactorModel}
import play.api.libs.json.{Json, OFormat}

case class EmailAddressChangeModel(
                                    templateId: String,
                                    vrn: String,
                                    formBundleReference: String,
                                    businessName: String,
                                    transactorDetails: TransactorModel,
                                    customerDetails: CustomerModel,
                                    preferences: PreferencesModel,
                                    originalEmailAddress: String
                                  )
  extends MessageModel(templateId, vrn, formBundleReference, businessName, transactorDetails, customerDetails, preferences) {
}

object EmailAddressChangeModel {
  implicit val formats: OFormat[EmailAddressChangeModel] = Json.format[EmailAddressChangeModel]
}
