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

package models

import common.Constants.SecureCommsMessageFields._
import models.secureMessageAlertModels._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Format, JsPath, Json, Reads, Writes}
import scala.language.postfixOps

case class SecureCommsMessageModel(
                                    templateId: String,
                                    vrn: String,
                                    formBundleReference: String,
                                    businessName: String,
                                    effectiveDateOfDeregistration: Option[String],
                                    addressDetails: Option[AddressDetailsModel],
                                    bankAccountDetails: Option[BankDetailsModel],
                                    staggerDetails: Option[StaggerDetailsModel],
                                    originalEmailAddress: Option[String],
                                    mandationStatus: Option[String],
                                    websiteAddress: Option[String],
                                    contactNumbers: Option[ContactNumbersModel],
                                    transactorDetails: TransactorModel,
                                    customerDetails: CustomerModel,
                                    preferences: PreferencesModel
                                  )

object SecureCommsMessageModel {
  def checkTupleForNone(input: Product): Boolean = {
    input.productIterator.map(_.asInstanceOf[Option[String]]).foldRight(false){ (element, currentStatus) =>
      if(!currentStatus) {
        element.nonEmpty
      } else { currentStatus }
    }
  }

  implicit val reads: Reads[SecureCommsMessageModel] = (
    (JsPath \ TEMPLATE_ID).read[String] and
      (JsPath \ VRN).read[String] and
      (JsPath \ FORM_BUNDLE_REFERENCE).read[String] and
      (JsPath \ BUSINESS_NAME).read[String] and
      (JsPath \ EFFECTIVE_DOD).readNullable[String] and
      ((JsPath \\ AL1).readNullable[String] and
        (JsPath \\ AL2).readNullable[String] and
        (JsPath \\ AL3).readNullable[String] and
        (JsPath \\ AL4).readNullable[String] and
        (JsPath \\ AL5).readNullable[String] and
        (JsPath \\ POST_CODE).readNullable[String] and
        (JsPath \\ COUNTRY_NAME).readNullable[String]
        tupled) and
      ((JsPath \\ ACCOUNT_NAME).readNullable[String] and
        (JsPath \\ ACCOUNT_NUMBER).readNullable[String] and
        (JsPath \\ SORT_CODE).readNullable[String]
        tupled) and
      ((JsPath \\ STAGGER).readNullable[String] and
        (JsPath \\ NEW_STAGGER_START_DATE).readNullable[String] and
        (JsPath \\ NEW_STAGGER_END_DATE).readNullable[String] and
        (JsPath \\ PREVIOUS_STAGGER).readNullable[String] and
        (JsPath \\ PREVIOUS_STAGGER_START_DATE).readNullable[String] and
        (JsPath \\ PREVIOUS_STAGGER_END_DATE).readNullable[String]
        tupled) and
      (JsPath \ O_EMAIL_ADDRESS).readNullable[String] and
      (JsPath \ MANDATION_STATUS).readNullable[String] and
      (JsPath \ WEBSITE_ADDRESS).readNullable[String] and
      ((JsPath \\ PRIMARY_PHONENUMBER).readNullable[String] and
        (JsPath \\ PRIMARY_PHONENUMBER_CHANGED).readNullable[String] and
        (JsPath \\ MOBILE_NUMBER).readNullable[String] and
        (JsPath \\ MOBILE_NUMBER_CHANGED).readNullable[String]
        tupled) and
      ((JsPath \\ TRANSACTOR_EMAIL).read[String] and
        (JsPath \\ TRANSACTOR_NAME).read[String]
        tupled) and
      ((JsPath \\ C_EMAIL).read[String] and
        (JsPath \\ C_EMAIL_STATUS).read[String]
        tupled) and
      ((JsPath \\ N_PREFS).read[String] and
        (JsPath \\ C_PREFS).read[String] and
        (JsPath \\ L_PREFS).read[String] and
        (JsPath \\ F_PREFS).read[String]
        tupled)
    ) { (tId, vrn, fbr, bs, edod, addDet, bankDet, staggerDet, oEmail, mandationStatus, website, conNums, tDet, cDet, prefDet) =>

    val addressDetails: Option[AddressDetailsModel] = if (checkTupleForNone(addDet)) {
      Some(AddressDetailsModel(
        addDet._1.getOrElse(""),
        addDet._2.getOrElse(""),
        addDet._3.getOrElse(""),
        addDet._4.getOrElse(""),
        addDet._5.getOrElse(""),
        addDet._6.getOrElse(""),
        addDet._7.getOrElse("")
      ))
    } else {
      None
    }

    val bankAccountDetails: Option[BankDetailsModel] = if (checkTupleForNone(bankDet)) {
      Some(BankDetailsModel(
        bankDet._1.getOrElse(""),
        bankDet._2.getOrElse(""),
        bankDet._3.getOrElse("")
      ))
    } else {
      None
    }

    val staggerDetails: Option[StaggerDetailsModel] = if (checkTupleForNone(staggerDet)) {
     Some(StaggerDetailsModel(
       staggerDet._1.getOrElse(""),
       staggerDet._2.getOrElse(""),
       staggerDet._3.getOrElse(""),
       staggerDet._4.getOrElse(""),
       staggerDet._5.getOrElse(""),
       staggerDet._6.getOrElse("")
     ))
    } else {
      None
    }

    val contactNumbers: Option[ContactNumbersModel] = if (checkTupleForNone(conNums)) {
      Some(ContactNumbersModel(
        conNums._1.getOrElse(""),
        conNums._2.getOrElse(""),
        conNums._3.getOrElse(""),
        conNums._4.getOrElse("")
      ))
    } else {
      None
    }

    SecureCommsMessageModel.apply(
      tId,
      vrn,
      fbr,
      bs,
      edod,
      addressDetails,
      bankAccountDetails,
      staggerDetails,
      oEmail,
      mandationStatus,
      website,
      contactNumbers,
      TransactorModel(tDet._1, tDet._2),
      CustomerModel(cDet._1, cDet._2),
      PreferencesModel(prefDet._1, prefDet._2, prefDet._3, prefDet._4)
    )
  }

  implicit val writes: Writes[SecureCommsMessageModel] = Json.writes[SecureCommsMessageModel]

  val format: Format[SecureCommsMessageModel] = Format(reads, writes)
}
