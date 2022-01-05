/*
 * Copyright 2022 HM Revenue & Customs
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
