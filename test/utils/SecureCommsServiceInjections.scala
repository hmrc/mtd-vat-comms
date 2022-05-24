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

package utils

import play.api.inject.Injector
import views.html._

class SecureCommsServiceInjections (injector: Injector) {
  val vatEmailApproved: VatEmailApproved = injector.instanceOf[VatEmailApproved]
  val vatEmailRejected: VatEmailRejected = injector.instanceOf[VatEmailRejected]
  val vatBankDetailsApproved: VatBankDetailsApproved = injector.instanceOf[VatBankDetailsApproved]
  val vatBankDetailsRejected: VatBankDetailsRejected = injector.instanceOf[VatBankDetailsRejected]
  val vatDeregApproved: VatDeregApproved = injector.instanceOf[VatDeregApproved]
  val vatDeregRejected: VatDeregRejected = injector.instanceOf[VatDeregRejected]
  val vatPPOBApproved: VatPPOBApproved = injector.instanceOf[VatPPOBApproved]
  val vatPPOBRejected: VatPPOBRejected = injector.instanceOf[VatPPOBRejected]
  val vatStaggerApproved: VatStaggerApproved = injector.instanceOf[VatStaggerApproved]
  val vatStaggerRejected: VatStaggerRejected = injector.instanceOf[VatStaggerRejected]
  val vatStaggerApprovedLeaveAnnualAccounting: VatStaggerApprovedLeaveAnnualAccounting = injector.instanceOf[VatStaggerApprovedLeaveAnnualAccounting]
  val vatContactNumberApproved: VatContactNumbersApproved = injector.instanceOf[VatContactNumbersApproved]
  val vatContactNumbersRejected: VatContactNumbersRejected = injector.instanceOf[VatContactNumbersRejected]
  val vatWebsiteApproved: VatWebsiteApproved = injector.instanceOf[VatWebsiteApproved]
  val vatWebsiteRejected: VatWebsiteRejected = injector.instanceOf[VatWebsiteRejected]
}
