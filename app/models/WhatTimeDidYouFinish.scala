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

package models

import play.api.libs.json.{JsError, JsString, JsSuccess, Json, OWrites, Reads}

import scala.util.matching.Regex

final case class WhatTimeDidYouFinish(hour: Int, minute: Int, amOrPm: String) {

  override def toString: String = f"$hour:$minute%02d$amOrPm"
}

object WhatTimeDidYouFinish {

  implicit lazy val writes: OWrites[WhatTimeDidYouFinish] = Json.writes[WhatTimeDidYouFinish]

  implicit lazy val reads: Reads[WhatTimeDidYouFinish] = Json.reads[WhatTimeDidYouFinish] orElse fallbackReads

  private val FallbackFormat: Regex = """^(?i)(\d|0\d|1[0-2])[:. ]?([0-5][0-9])? ?([ap]\.?m\.?)$""".r

  // TODO remove after release
  private val fallbackReads: Reads[WhatTimeDidYouFinish] = Reads {
    case JsString(FallbackFormat(rawHour, rawMinute, rawAmPm)) =>
      val hour = rawHour.toInt
      val minute = Option(rawMinute).map(_.toInt).getOrElse(0)
      val amPm = rawAmPm.toLowerCase.replaceAll("\\.", "")
      JsSuccess(WhatTimeDidYouFinish(hour, minute, amPm))
    case _ =>
      JsError("Invalid")
  }
}
