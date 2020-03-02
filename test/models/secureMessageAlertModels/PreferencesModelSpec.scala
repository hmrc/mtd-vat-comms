/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.secureMessageAlertModels

import base.BaseSpec
import play.api.libs.json.{JsObject, Json}
import common.Constants.NotificationPreference._
import common.Constants.ChannelPreferences._
import common.Constants.FormatPreferences._
import common.Constants.LanguagePreferences._

class PreferencesModelSpec extends BaseSpec {
  val notificationPrefs: Seq[String] = Seq(EMAIL, SMS)
  val channelPrefs: Seq[String] = Seq(DIGITAL, PAPER)
  val languagePrefs: Seq[String] = Seq(ENGLISH, WELSH)
  val formatPrefs: Seq[String] = Seq(TEXT, BRAILLE, LARGE_PRINT, AUDIO)

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
      "notificationPreference" -> np,
      "channelPreference" -> cp,
      "languagePreference" -> lp,
      "formatPreference" -> fp
    )

    s"Preferences model with inputs: $np, $cp, $lp, $fp" should {
      "parse from the correct json structure" in {
        validJson.as[PreferencesModel] shouldBe expectedModel
      }
    }
  }
}
