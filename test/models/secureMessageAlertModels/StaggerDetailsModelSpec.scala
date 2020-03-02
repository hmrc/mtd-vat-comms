/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.play.test.UnitSpec

class StaggerDetailsModelSpec extends UnitSpec {

  val expectedModelForConfirmation: StaggerDetailsModel = StaggerDetailsModel(
    "stagger",
    "newStaggerStartDate",
    "newStaggerPeriodEndDate",
    "previousStagger",
    "previousStaggerStartDate",
    "previousStaggerEndDate"
  )

  val expectedModelForRejection: StaggerDetailsModel = StaggerDetailsModel(
    "stagger",
    "",
    "",
    "",
    "",
    ""
  )

  val validJsonForConfirmation: JsObject = Json.obj(
    "stagger" -> "stagger",
    "newStaggerStartDate" -> "newStaggerStartDate",
    "newStaggerPeriodEndDate" -> "newStaggerPeriodEndDate",
    "previousStagger" -> "previousStagger",
    "previousStaggerStartDate" -> "previousStaggerStartDate",
    "previousStaggerEndDate" -> "previousStaggerEndDate"
  )

  val validJsonForRejection: JsObject = Json.obj(
    "stagger" -> "stagger",
    "newStaggerStartDate" -> "",
    "newStaggerPeriodEndDate" -> "",
    "previousStagger" -> "",
    "previousStaggerStartDate" -> "",
    "previousStaggerEndDate" -> ""
  )

  "StaggerDetails model with all fields" should {
    "parse from the correct json structure" in {
      validJsonForConfirmation.as[StaggerDetailsModel] shouldBe expectedModelForConfirmation
    }
  }

  "StaggerDetails model with one field" should {
    "parse from the correct json structure" in {
      validJsonForRejection.as[StaggerDetailsModel] shouldBe expectedModelForRejection
    }
  }

  "StaggerDetails model writes for a valid confirmation" should {
    "parse from the correct json structure" in {
      StaggerDetailsModel.formats.writes(expectedModelForConfirmation) shouldBe validJsonForConfirmation
    }
  }

  "StaggerDetails model writes for a valid rejection" should {
    "parse from the correct json structure" in {
      StaggerDetailsModel.formats.writes(expectedModelForRejection) shouldBe validJsonForRejection
    }
  }
}
