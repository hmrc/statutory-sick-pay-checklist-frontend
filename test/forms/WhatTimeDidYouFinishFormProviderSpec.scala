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

package forms

import forms.behaviours.{IntFieldBehaviours, StringFieldBehaviours}
import models.WhatTimeDidYouFinish
import org.scalacheck.{Gen, Shrink}
import play.api.data.FormError

class WhatTimeDidYouFinishFormProviderSpec extends StringFieldBehaviours with IntFieldBehaviours {

  implicit val dontShrinkInt: Shrink[Int] = Shrink.shrinkAny

  val form = new WhatTimeDidYouFinishFormProvider()()

  val invalidKey = "whatTimeDidYouFinish.error.invalid"
  val requiredKey = "whatTimeDidYouFinish.error.required"
  val twoRequiredKey = "whatTimeDidYouFinish.error.required.two"
  val amPmRequiredKey = "whatTimeDidYouFinish.error.required.ampm"
  val overallInvalidKey = "whatTimeDidYouFinish.error.invalid.overall"

  ".time-finished" - {

    "must bind valid values" in {
      val gen = for {
        hour <- Gen.chooseNum(1, 12)
        minute <- Gen.chooseNum(0, 59)
        amPm <- Gen.oneOf("am", "pm")
      } yield (hour, minute, amPm)

      forAll(gen -> "valid time") { time =>

        val data = Map(
          "time-finished.hour" -> time._1.toString,
          "time-finished.minute" -> time._2.toString,
          "time-finished.ampm" -> time._3
        )

        val result = form.bind(data)

        result.errors mustBe empty
        result.value.value mustEqual WhatTimeDidYouFinish(time._1, time._2, time._3)

      }
    }

    "must not bind when hour is missing" in {
      val gen = for {
        minute <- Gen.chooseNum(0, 59)
        amPm <- Gen.oneOf("am", "pm")
      } yield (minute, amPm)

      forAll(gen -> "valid time") { time =>
        val data = Map(
          "time-finished.minute" -> time._1.toString,
          "time-finished.ampm" -> time._2
        )

        val result = form.bind(data)
        result.errors must contain only FormError("time-finished", requiredKey, List("hour"))
      }
    }

    "must not bind when minute is missing" in {
      val gen = for {
        hour <- Gen.chooseNum(0, 23)
        amPm <- Gen.oneOf("am", "pm")
      } yield (hour, amPm)

      forAll(gen -> "valid time") { time =>
        val data = Map(
          "time-finished.hour" -> time._1.toString,
          "time-finished.ampm" -> time._2
        )

        val result = form.bind(data)
        result.errors must contain only FormError("time-finished", requiredKey, List("minute"))
      }
    }

    "must not bind hours outside of 0 to 23" in {
      val gen = for {
        hour <- intsOutsideRange(0, 23)
        minute <- Gen.chooseNum(0, 59)
        amPm <- Gen.oneOf("am", "pm")
      } yield (hour, minute, amPm)

      forAll(gen -> "valid time") { time =>
        val data = Map(
          "time-finished.hour" -> time._1.toString,
          "time-finished.minute" -> time._2.toString,
          "time-finished.ampm" -> time._3
        )

        val result = form.bind(data)
        result.errors must contain only FormError("time-finished", invalidKey, List("hour"))
      }
    }

    "must not bind minutes outside of 0 to 59" in {
      val gen = for {
        hour <- Gen.chooseNum(0, 23)
        minute <- intsOutsideRange(0, 59)
        amPm <- Gen.oneOf("am", "pm")
      } yield (hour, minute, amPm)

      forAll(gen -> "valid time") { time =>
        val data = Map(
          "time-finished.hour" -> time._1.toString,
          "time-finished.minute" -> time._2.toString,
          "time-finished.ampm" -> time._3
        )

        val result = form.bind(data)
        result.errors must contain only FormError("time-finished", invalidKey, List("minute"))
      }
    }

    "must not bind invalid am/pm values" in {
      val gen = for {
        hour <- Gen.chooseNum(0, 23)
        minute <- Gen.chooseNum(0, 59)
        amPm <- stringsExceptSpecificValues(List("am", "pm"))
      } yield (hour, minute, amPm)

      forAll(gen -> "valid time") { time =>
        val data = Map(
          "time-finished.hour" -> time._1.toString,
          "time-finished.minute" -> time._2.toString,
          "time-finished.ampm" -> time._3
        )

        val result = form.bind(data)
        result.errors must contain only FormError("time-finished", amPmRequiredKey, List("ampm"))
      }
    }

  }
}
