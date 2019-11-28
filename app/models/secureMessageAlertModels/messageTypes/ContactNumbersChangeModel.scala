/*
 * Copyright 2019 HM Revenue & Customs
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