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

package utils

object SecureCommsMessageBodyStrings {
  val validEDODString: String = "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
    "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p>" +
    "<p>EFFECTIVE DATE OF DE-REGISTRATION|20181227</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
    "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p>" +
    "<p>CUSTOMER EMAIL STATUS|VERIFIED</p><p>NOTIFICATION PREFERENCE|EMAIL</p><p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p>" +
    "<p>FORMAT PREFERENCE|TEXT</p><p>TRANSACTOR NAME|DRAGON BORN</p>"

  val validPPOBChangeString: String = "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
    "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
    "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p><p>CUSTOMER EMAIL STATUS|VERIFIED</p><p>NOTIFICATION PREFERENCE|EMAIL</p>" +
    "<p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p><p>FORMAT PREFERENCE|TEXT</p><p>TRANSACTOR NAME|DRAGON BORN</p>" +
    "<p>ADDRESS LINE1|</p><p>ADDRESS LINE2|</p><p>ADDRESS LINE3|</p><p>ADDRESS LINE4|</p><p>ADDRESS LINE5|</p>" +
    "<p>POST CODE|</p><p>COUNTRY NAME|</p>"

  val validBankAccountChangeString: String = "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
    "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
    "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p><p>CUSTOMER EMAIL STATUS|VERIFIED</p><p>NOTIFICATION PREFERENCE|EMAIL</p>" +
    "<p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p><p>FORMAT PREFERENCE|TEXT</p><p>TRANSACTOR NAME|DRAGON BORN</p>" +
    "<p>BANK ACCOUNT NUMBER|</p><p>BANK SORT CODE|</p>"

  val validStaggerChangeString: String = "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
    "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
    "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p><p>CUSTOMER EMAIL STATUS|VERIFIED</p><p>NOTIFICATION PREFERENCE|EMAIL</p>" +
    "<p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p><p>FORMAT PREFERENCE|TEXT</p><p>TRANSACTOR NAME|DRAGON BORN</p>" +
    "<p>STAGGER|</p>"

  val validEmailChangeString: String = "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
    "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
    "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p><p>CUSTOMER EMAIL STATUS|VERIFIED</p><p>NOTIFICATION PREFERENCE|EMAIL</p>" +
    "<p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p><p>FORMAT PREFERENCE|TEXT</p><p>TRANSACTOR NAME|DRAGON BORN</p>" +
    "<p>ORIGINAL EMAIL ADDRESS|</p>"

  val validBusinessNameChangeString: String = "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
    "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
    "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p><p>CUSTOMER EMAIL STATUS|VERIFIED</p><p>NOTIFICATION PREFERENCE|EMAIL</p>" +
    "<p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p><p>FORMAT PREFERENCE|TEXT</p><p>TRANSACTOR NAME|DRAGON BORN</p>"

  val invalidEDODString: String = "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
    "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p>" +
    "<p>EFFECTIVE DATE OF DE-REGISTRATION|20181227</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
    "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p>" +
    "<p>CUSTOMER EMAIL STATUS|VERIFIED</p><p>NOTIFICATION PREFERENCE|EMAIL</p><p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p>"

  val invalidJsonParseString: String = "<p>TEMPLATE-ID|VRT41A_SM1A</p><p>VRN|100065579</p>" +
    "<p>FORM BUNDLE REFERENCE|092000003080</p><p>BUSINESS NAME|CoC Company Holdings Ltd</p>" +
    "<p>EFFECTIVE DATE OF DE-REGISTRATION|20181227</p><p>TRANSACTOR EMAIL|Info_in_FB@CoCHoldingsLtd.co.uk</p>" +
    "<p>CUSTOMER EMAIL|info@CoCHoldings.co.uk</p>" +
    "<p>CUSTOMER EMAIL STATUS</p><p>NOTIFICATION PREFERENCE|EMAIL</p><p>CHANNEL PREFERENCE|PAPER</p><p>LANGUAGE PREFERENCE|ENGLISH</p>"
}
