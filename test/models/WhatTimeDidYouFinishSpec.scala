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

import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import play.api.libs.json.{JsString, Json}

class WhatTimeDidYouFinishSpec extends AnyFreeSpec with Matchers with OptionValues {

  ".toString" - {

    "must return the correct string" in {
      List(
        WhatTimeDidYouFinish(12, 0, "pm") -> "12:00pm",
        WhatTimeDidYouFinish(4, 30, "am") -> "4:30am"
      ).foreach { case (time, expected) =>
        time.toString mustEqual expected
      }
    }
  }

  "must read from json object" in {
    val json = Json.obj(
      "hour" -> 12,
      "minute" -> 0,
      "amOrPm" -> "am"
    )
    val expected = WhatTimeDidYouFinish(12, 0, "am")
    Json.fromJson[WhatTimeDidYouFinish](json).asOpt.value mustEqual expected
  }

  List(
    "9am" -> WhatTimeDidYouFinish(9, 0, "am"),
    "08AM" -> WhatTimeDidYouFinish(8, 0, "am"),
    "07 00a.m." -> WhatTimeDidYouFinish(7, 0, "am"),
    "10:01 am" -> WhatTimeDidYouFinish(10, 1, "am"),
    "10.30PM" -> WhatTimeDidYouFinish(10, 30, "pm")
  ).foreach { case (string, expected) =>
    s"must read from a string ($string)" in {
      Json.fromJson[WhatTimeDidYouFinish](JsString(string)).asOpt.value mustEqual expected
    }
  }

  "must fail to read from an invalid string" in {
    Json.fromJson[WhatTimeDidYouFinish](JsString("foobar")).asOpt mustBe empty
  }
}
