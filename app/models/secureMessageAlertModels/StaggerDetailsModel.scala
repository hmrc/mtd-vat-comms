/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import play.api.libs.json.{Json, OFormat}

case class StaggerDetailsModel(
                                stagger: String,
                                newStaggerStartDate: String,
                                newStaggerPeriodEndDate: String,
                                previousStagger: String,
                                previousStaggerStartDate: String,
                                previousStaggerEndDate: String
                              )

  object StaggerDetailsModel {
  implicit val formats: OFormat[StaggerDetailsModel] = Json.format[StaggerDetailsModel]
}



