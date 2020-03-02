/*
 * Copyright 2020 HM Revenue & Customs
 *
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
    "VRT14A_SM10A" -> false,
    // opt out client approved
    "CC07A_SM11A" -> true,
    // opt out agent approved
    "CC07C_SM11C" -> true,
    // website agent approved
    "VRT12C_SM14C" -> true,
    // website agent rejected
    "VRT14C_SM15C" -> false,
    // website client approved
    "VRT12A_SM14A" -> true,
    // website client rejected
    "VRT14A_SM15A" -> false,
    //contact numbers agent approved
    "VRT12C_SM12C" -> true,
    //contact numbers agent rejected
    "VRT14C_SM13C" -> false,
    //contact numbers client approved
    "VRT12A_SM12A" -> true,
    //contact numbers client rejected
    "VRT14A_SM13A" -> false
  )

  def isTemplateIdApproval(templateId: String): Option[Boolean] =
    templateIdApprovalMap.filterKeys(_ == templateId).values.headOption

}
