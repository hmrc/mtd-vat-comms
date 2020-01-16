/*
 * Copyright 2020 HM Revenue & Customs
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

package controllers

import play.api.http.Status.NO_CONTENT
import common.ApiConstants._
import helpers.IntegrationBaseSpec
import play.api.libs.ws.WSRequest

class OptOutControllerISpec extends IntegrationBaseSpec {

  "/events/opt-out" should {

    "return a success response" when {

      "request body json is an opt out" in {

        val request: WSRequest = buildRequest("/events/opt-out")

        val response = await(request.post(vatChangeEventJson("Opt Out")))

        response.status shouldBe NO_CONTENT
      }
    }
  }
}
