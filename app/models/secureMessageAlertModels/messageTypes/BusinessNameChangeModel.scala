/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels.messageTypes

import models.secureMessageAlertModels.{CustomerModel, PreferencesModel, TransactorModel}
import play.api.libs.json.{Json, OFormat}

case class BusinessNameChangeModel(
                                    templateId: String,
                                    vrn: String,
                                    formBundleReference: String,
                                    businessName: String,
                                    transactorDetails: TransactorModel,
                                    customerDetails: CustomerModel,
                                    preferences: PreferencesModel
                                  )
  extends MessageModel(templateId, vrn, formBundleReference, businessName, transactorDetails, customerDetails, preferences) {
}

object BusinessNameChangeModel {
  implicit val formats: OFormat[BusinessNameChangeModel] = Json.format[BusinessNameChangeModel]
}
