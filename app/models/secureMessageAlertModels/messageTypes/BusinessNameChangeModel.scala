/*
 * Copyright 2024 HM Revenue & Customs
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

import models.secureMessageAlertModels.{CustomerModel, PreferencesModel, TransactorModel}
import play.api.libs.json.{Json, OFormat}

case class BusinessNameChangeModel(override val templateId: String,
                                   override val vrn: String,
                                   formBundleReference: String,
                                   override val businessName: String,
                                   override val transactorDetails: TransactorModel,
                                   customerDetails: CustomerModel,
                                   preferences: PreferencesModel)
  extends MessageModel(templateId, vrn, formBundleReference, businessName, transactorDetails, customerDetails, preferences) {
}

object BusinessNameChangeModel {
  implicit val formats: OFormat[BusinessNameChangeModel] = Json.format[BusinessNameChangeModel]
}
