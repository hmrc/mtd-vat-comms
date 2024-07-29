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

package mocks

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.http.HttpClient

import scala.concurrent.Future

trait MockHttpClient extends AnyWordSpecLike with Matchers with MockitoSugar with BeforeAndAfterEach {

  val mockHttpClient: HttpClient = mock[HttpClient]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockHttpClient)
  }

  def mockHttpGet[A](response: Future[A]): Unit =
    when(mockHttpClient.GET[A]
      (any(), any(), any())
      (any(), any(), any()))
      .thenReturn(response)

  def mockHttpPost[I, O](response: Future[O]): Unit =
    when(mockHttpClient.POST[I, O]
      (any(), any(), any())
      (any(), any(), any(), any()))
      .thenReturn(response)
}
