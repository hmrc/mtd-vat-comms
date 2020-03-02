/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels.messageTypes

import models.secureMessageAlertModels.{PreferencesModel, CustomerModel, TransactorModel}
import play.api.libs.json.{Json, OFormat}

case class OptOutModel(
                                    templateId: String,
                                    vrn: String,
                                    formBundleReference: String,
                                    businessName: String,
                                    transactorDetails: TransactorModel,
                                    customerDetails: CustomerModel,
                                    mandationStatus: String,
                                    preferences: PreferencesModel
                                  )
  extends MessageModel(templateId, vrn, formBundleReference, businessName, transactorDetails, customerDetails, preferences) {
}

object OptOutModel {
  implicit val formats: OFormat[OptOutModel] = Json.format[OptOutModel]
}
