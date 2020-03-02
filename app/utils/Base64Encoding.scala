/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package utils

import java.nio.charset.StandardCharsets
import java.util.Base64

object Base64Encoding {
  private val encoder: Base64.Encoder = Base64.getEncoder
  private val decoder: Base64.Decoder = Base64.getDecoder

  def encode(text: String): String = encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8))
  def decode(text: String): String = new String(decoder.decode(text))

}
