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

package models.secureCommsModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}
import utils.Constants.NotificationPreference._
import utils.Constants.ChannelPreferences._
import utils.Constants.FormatPreferences._
import utils.Constants.LanguagePreferences._

class PreferencesModelSpec extends BaseSpec {
  val notificationPrefs = Seq(EMAIL, SMS)
  val channelPrefs = Seq(DIGITAL, PAPER)
  val languagePrefs = Seq(ENGLISH, WELSH)
  val formatPrefs = Seq(TEXT, BRAILLE, LARGE_PRINT, AUDIO)

  for {
    np <- notificationPrefs
    cp <- channelPrefs
    lp <- languagePrefs
    fp <- formatPrefs
  } yield {
    val expectedModel: PreferencesModel = PreferencesModel(
      np, cp, lp, fp
    )

    val validJson: JsObject = Json.obj(
      "notificationPreferences" -> np,
      "channelPreferences" -> cp,
      "languagePreferences" -> lp,
      "formatPreferences" -> fp
    )

    s"AddressDetails model with inputs: $np, $cp, $lp, $fp" should {
      "parse from the correct json structure" in {
        validJson.as[PreferencesModel] shouldBe expectedModel
      }
    }
  }
}
