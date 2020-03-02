/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package utils
import base.BaseSpec

class Base64EncodingSpec extends BaseSpec {

  val testData: String = "This is some test data"
  val expectedEncodedValue: String = "VGhpcyBpcyBzb21lIHRlc3QgZGF0YQ=="

  "Base 64 Encoder" should {
    "encode to the expected value" in {

      val encoded = Base64Encoding.encode(testData)
      encoded shouldBe expectedEncodedValue
    }
  }

  "Base 64 Encoder" should {
    "decode to the expected value" in {

      Base64Encoding.decode(expectedEncodedValue) shouldBe testData
    }
  }
}
