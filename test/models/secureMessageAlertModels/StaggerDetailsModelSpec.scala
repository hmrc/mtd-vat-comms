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

package models.secureMessageAlertModels

import play.api.libs.json.{JsObject, Json}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class StaggerDetailsModelSpec extends AnyWordSpecLike with Matchers {

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
