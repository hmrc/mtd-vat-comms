/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels.messageTypes

import models.secureMessageAlertModels.{CustomerModel, PreferencesModel, TransactorModel}
import play.api.libs.json.{Json, OFormat}

case class WebAddressChangeModel(
                                    templateId: String,
                                    vrn: String,
                                    formBundleReference: String,
                                    businessName: String,
                                    transactorDetails: TransactorModel,
                                    customerDetails: CustomerModel,
                                    websiteAddress: String,
                                    preferences: PreferencesModel
                                  )
  extends MessageModel(templateId, vrn, formBundleReference, businessName, transactorDetails, customerDetails, preferences) {
}

object WebAddressChangeModel {
  implicit val formats: OFormat[WebAddressChangeModel] = Json.format[WebAddressChangeModel]
}
