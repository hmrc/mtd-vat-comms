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

package models

import models.SecureCommsModels._
import play.api.libs.json.{Json, Reads, Writes}

case class SecureCommsMessageModel(
                                    templateId: String,
                                    vrn: String,
                                    formBundleReference: String,
                                    businessName: String,
                                    effectiveDateOfRegistration: Option[String],
                                    addressDetails: Option[AddressDetailsModel],
                                    bankAccountDetails: Option[BankDetailsModel],
                                    stagger: Option[String],
                                    originalEmailAddress: Option[String],
                                    transactorDetails: TransactorModel,
                                    customerDetails: CustomerModel,
                                    preferences: PreferencesModel
                                  )

object SecureCommsMessageModel {
  implicit val reads: Reads[SecureCommsMessageModel] = Json.reads[SecureCommsMessageModel]
  implicit val writes: Writes[SecureCommsMessageModel] = Json.writes[SecureCommsMessageModel]
}
