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

package utils

object TemplateMappings {

  private val templateIdApprovalMap: Map[String, Boolean] = Map(

    // ppob transactor approved
    "VRT12C_SM1C" -> true,
    // ppob transactor rejected
    "VRT14C_SM2C" -> false,
    // bank details transactor approved
    "VRT12C_SM3C" -> true,
    // bank details transactor rejected
    "VRT14C_SM4C" -> false,
    // stagger transactor approved
    "VRT12C_SM5C" -> true,
    // stagger transactor rejected
    "VRT14C_SM6C" -> false,
    // dereg transactor approved
    "VRT23C_SM7C" -> true,
    // dereg transactor rejected
    "VRT15C_SM8C" -> false,
    // ppob client approved
    "VRT12A_SM1A" -> true,
    // ppob client rejected
    "VRT14A_SM2A" -> false,
    // bank details client approved
    "VRT12A_SM3A" -> true,
    // bank details client rejected
    "VRT14A_SM4A" -> false,
    // stagger client approved
    "VRT12A_SM5A" -> true,
    // stagger client rejected
    "VRT14A_SM6A" -> false,
    // dereg client approved
    "VRT23A_SM7A" -> true,
    // dereg client rejected
    "VRT15A_SM8A" -> false,
    // email client approved
    "VRT12A_SM9A" -> true,
    // email client rejected
    "VRT14A_SM10A" -> false
  )

  def isTemplateIdApproval(templateId: String): Boolean = {
    templateIdApprovalMap.filterKeys(_ == templateId).values.head
  }

}
