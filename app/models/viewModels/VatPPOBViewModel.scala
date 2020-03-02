/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.viewModels

case class VatPPOBViewModel(addressLine1: String,
                            addressLine2: String,
                            addressLine3: Option[String],
                            addressLine4: Option[String],
                            addressLine5: Option[String],
                            postcode: Option[String],
                            country: Option[String])
