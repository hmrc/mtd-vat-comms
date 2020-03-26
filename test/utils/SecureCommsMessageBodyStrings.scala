/*
 * Copyright 2020 HM Revenue & Customs
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
  val validEDODString: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|123456789</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|testBusinessName</P>" +
    "<P>EFFECTIVE DATE OF DEREGISTRATION|20181227</P><P>TRANSACTOR EMAIL|test@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL|testCustomer@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL STATUS|VERIFIED</P><P>NOTIFICATION PREFERENCE|EMAIL</P><P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P>" +
    "<P>FORMAT PREFERENCE|TEXT</P><P>TRANSACTOR NAME|testTransactorName</P>]]>"

  val validPPOBChangeString: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|123456789</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|testBusinessName</P><P>TRANSACTOR EMAIL|lydia@carryburdens.tam</P>" +
    "<P>CUSTOMER EMAIL|dovahkin@riften.tam</P><P>CUSTOMER EMAIL STATUS|VERIFIED</P><P>NOTIFICATION PREFERENCE|EMAIL</P>" +
    "<P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P><P>FORMAT PREFERENCE|TEXT</P><P>TRANSACTOR NAME|testTransactorName</P>" +
    "<P>ADDRESS LINE1|</P><P>ADDRESS LINE2|</P><P>ADDRESS LINE3|</P><P>ADDRESS LINE4|</P><P>ADDRESS LINE5|</P>" +
    "<P>POST CODE|</P><P>COUNTRY NAME|</P>]]>"

  val validBankAccountChangeString: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|123456789</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|testBusinessName</P><P>TRANSACTOR EMAIL|testTransactor@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL|testCustomer@email.co.uk</P><P>CUSTOMER EMAIL STATUS|VERIFIED</P><P>NOTIFICATION PREFERENCE|EMAIL</P>" +
    "<P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P><P>FORMAT PREFERENCE|TEXT</P><P>TRANSACTOR NAME|DRAGON BORN</P>" +
    "<P>BANK ACCOUNT NAME|</P><P>BANK ACCOUNT NUMBER|</P><P>BANK SORT CODE|</P>]]>"

  val validStaggerChangeString: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|123456789</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|testBusinessName</P><P>TRANSACTOR EMAIL|testTransactor@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL|testCustomer@email.co.uk</P><P>CUSTOMER EMAIL STATUS|VERIFIED</P><P>NOTIFICATION PREFERENCE|EMAIL</P>" +
    "<P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P><P>FORMAT PREFERENCE|TEXT</P><P>TRANSACTOR NAME|DRAGON BORN</P>" +
    "<P>STAGGER|</P>]]>"

  val validEmailChangeString: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|123456789</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|testBusinessName</P><P>TRANSACTOR EMAIL|testTransactor@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL|testCustomer@email.co.uk</P><P>CUSTOMER EMAIL STATUS|VERIFIED</P><P>NOTIFICATION PREFERENCE|EMAIL</P>" +
    "<P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P><P>FORMAT PREFERENCE|TEXT</P><P>TRANSACTOR NAME|DRAGON BORN</P>" +
    "<P>ORIGINAL EMAIL ADDRESS|</P>]]>"

  val validBusinessNameChangeString: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|123456789</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|testBusinessName</P><P>TRANSACTOR EMAIL|testTransactor@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL|testCustomer@email.co.uk</P><P>CUSTOMER EMAIL STATUS|VERIFIED</P><P>NOTIFICATION PREFERENCE|EMAIL</P>" +
    "<P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P><P>FORMAT PREFERENCE|TEXT</P><P>TRANSACTOR NAME|DRAGON BORN</P>]]>"

  val invalidEDODString: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|123456789</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|testBusinessName</P>" +
    "<P>EFFECTIVE DATE OF DEREGISTRATION|20181227</P><P>TRANSACTOR EMAIL|testTransactor@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL|testCustomer@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL STATUS|VERIFIED</P><P>NOTIFICATION PREFERENCE|EMAIL</P><P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P>]]>"

  val invalidJsonParseString: String = "<![CDATA[<P>TEMPLATE-ID|VRT41A_SM1A</P><P>VRN|123456789</P>" +
    "<P>FORM BUNDLE REFERENCE|092000003080</P><P>BUSINESS NAME|testBusinessName</P>" +
    "<P>EFFECTIVE DATE OF DEREGISTRATION|20181227</P><P>TRANSACTOR EMAIL|testTransactor@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL|testCustomer@email.co.uk</P>" +
    "<P>CUSTOMER EMAIL STATUS</P><P>NOTIFICATION PREFERENCE|EMAIL</P><P>CHANNEL PREFERENCE|PAPER</P><P>LANGUAGE PREFERENCE|ENGLISH</P>]]>"
}
