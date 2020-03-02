/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels.messageTypes

import models.secureMessageAlertModels.{CustomerModel, PreferencesModel, StaggerDetailsModel, TransactorModel}
import play.api.libs.json.{Json, OFormat}

case class VATStaggerChangeModel(
                                  templateId: String,
                                  vrn: String,
                                  formBundleReference: String,
                                  businessName: String,
                                  transactorDetails: TransactorModel,
                                  customerDetails: CustomerModel,
                                  preferences: PreferencesModel,
                                  staggerDetails: StaggerDetailsModel
                                )
  extends MessageModel(templateId, vrn, formBundleReference, businessName, transactorDetails, customerDetails, preferences) {
}

object VATStaggerChangeModel {
  implicit val formats: OFormat[VATStaggerChangeModel] = Json.format[VATStaggerChangeModel]
}
