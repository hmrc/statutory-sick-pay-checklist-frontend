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

package forms.mappings

import generators.Generators
import models.WhatTimeDidYouFinish
import org.scalacheck.{Gen, Shrink}
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.{Form, FormError}

class TimeMappingsSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with Generators with OptionValues
  with Mappings {

  implicit val dontShrinkInt: Shrink[Int] = Shrink.shrinkAny

  val form = Form(
    "value" -> timeAmPm(
      "error.invalid",
      "error.required",
      "error.required.two",
      "error.required.ampm",
      "error.invalid.hour"
    )
  )

  "must bind 12hr times" in {

    val gen = for {
      hour <- Gen.chooseNum(1, 12)
      minute <- Gen.chooseNum(0, 59)
      amPm <- Gen.oneOf("am", "pm")
    } yield (hour, minute, amPm)

    forAll(gen -> "valid time") { time =>

      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)

      result.errors mustBe empty
      result.value.value mustEqual WhatTimeDidYouFinish(time._1, time._2, time._3)

    }

  }

  "must not bind PM times with 0 hour" in {

    val gen = for {
      minute <- Gen.chooseNum(0, 59)
    } yield (0, minute, "pm")

    forAll(gen -> "valid time") { time =>

      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.invalid.hour", List.empty)

    }

  }

  "must not bind hours 1 to 12 without AM or PM" in {

    val gen = for {
      hour <- Gen.chooseNum(1, 12)
      minute <- Gen.chooseNum(0, 59)
    } yield (hour, minute, "")

    forAll(gen -> "valid time") { time =>

      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)

      result.errors must contain only FormError("value", "error.required.ampm", List.empty)

    }

  }

  "must bind 24hr times with 0 hour and AM given" in {

    val gen = for {
      minute <- Gen.chooseNum(0, 59)
    } yield (0, minute, "am")

    forAll(gen -> "valid time") { time =>

      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)

      result.errors mustBe empty
      result.value.value mustEqual WhatTimeDidYouFinish(12, time._2, time._3)

    }

  }

  "must bind 24hr times with 0 hour and AM missing" in {

    val gen = for {
      minute <- Gen.chooseNum(0, 59)
    } yield (0, minute, "")

    forAll(gen -> "valid time") { time =>

      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)

      result.errors mustBe empty
      result.value.value mustEqual WhatTimeDidYouFinish(12, time._2, "am")

    }

  }

  "must bind 24hr times when hour > 12 and PM given" in {

    val gen = for {
      hour <- Gen.chooseNum(13, 23)
      minute <- Gen.chooseNum(0, 59)
    } yield (hour, minute, "pm")

    forAll(gen -> "valid time") { time =>

      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)

      result.errors mustBe empty
      result.value.value mustEqual WhatTimeDidYouFinish(time._1 - 12, time._2, time._3)

    }

  }

  "must bind 24hr times when hour > 12 and PM missing" in {

    val gen = for {
      hour <- Gen.chooseNum(13, 23)
      minute <- Gen.chooseNum(0, 59)
    } yield (hour, minute, "")

    forAll(gen -> "valid time") { time =>

      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)

      result.errors mustBe empty
      result.value.value mustEqual WhatTimeDidYouFinish(time._1 - 12, time._2, "pm")

    }

  }

  "must not bind 24hr times when hour > 12 and AM given" in {

    val gen = for {
      hour <- Gen.chooseNum(13, 23)
      minute <- Gen.chooseNum(0, 59)
    } yield (hour, minute, "am")

    forAll(gen -> "valid time") { time =>

      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)

      result.errors must contain only FormError("value", "error.invalid.hour", List.empty)

    }

  }

  "must not bind when hour is missing" in {
    val gen = for {
      minute <- Gen.chooseNum(0, 59)
      amPm <- Gen.oneOf("am", "pm")
    } yield (minute, amPm)

    forAll(gen -> "valid time") { time =>
      val data = Map(
        "value.minute" -> time._1.toString,
        "value.ampm" -> time._2
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.required", List("hour"))
    }
  }

  "must not bind when minute is missing" in {
    val gen = for {
      hour <- Gen.chooseNum(0, 23)
      amPm <- Gen.oneOf("am", "pm")
    } yield (hour, amPm)

    forAll(gen -> "valid time") { time =>
      val data = Map(
        "value.hour" -> time._1.toString,
        "value.ampm" -> time._2
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.required", List("minute"))
    }
  }

  "must not bind when hour and minute are missing" in {
    val gen = for {
      amPm <- Gen.oneOf("am", "pm")
    } yield amPm

    forAll(gen -> "valid time") { time =>
      val data = Map(
        "value.ampm" -> time
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.required.two", List("hour", "minute"))
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
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.invalid", List("hour"))
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
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.invalid", List("minute"))
    }
  }

  "must not bind non-numeric hours" in {
    val gen = for {
      hour <- nonNumerics
      minute <- Gen.chooseNum(0, 59)
      amPm <- Gen.oneOf("am", "pm")
    } yield (hour, minute, amPm)

    forAll(gen -> "valid time") { time =>
      val data = Map(
        "value.hour" -> time._1,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.invalid", List("hour"))
    }
  }

  "must not bind non-numeric minutes" in {
    val gen = for {
      hour <- Gen.chooseNum(0, 23)
      minute <- nonNumerics
      amPm <- Gen.oneOf("am", "pm")
    } yield (hour, minute, amPm)

    forAll(gen -> "valid time") { time =>
      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.invalid", List("minute"))
    }
  }

  "must not bind decimal hours" in {
    val gen = for {
      hour <- decimals
      minute <- Gen.chooseNum(0, 59)
      amPm <- Gen.oneOf("am", "pm")
    } yield (hour, minute, amPm)

    forAll(gen -> "valid time") { time =>
      val data = Map(
        "value.hour" -> time._1,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.invalid", List("hour"))
    }
  }

  "must not bind decimal minutes" in {
    val gen = for {
      hour <- Gen.chooseNum(0, 23)
      minute <- decimals
      amPm <- Gen.oneOf("am", "pm")
    } yield (hour, minute, amPm)

    forAll(gen -> "valid time") { time =>
      val data = Map(
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.invalid", List("minute"))
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
        "value.hour" -> time._1.toString,
        "value.minute" -> time._2.toString,
        "value.ampm" -> time._3
      )

      val result = form.bind(data)
      result.errors must contain only FormError("value", "error.required.ampm", List.empty)
    }
  }

  "must unbind a time" in {
    val gen = for {
      hour <- Gen.chooseNum(1, 12)
      minute <- Gen.chooseNum(0, 59)
      amPm <- Gen.oneOf("am", "pm")
    } yield WhatTimeDidYouFinish(hour, minute, amPm)

    forAll(gen -> "valid time") { time =>

      val filledForm = form.fill(time)

      filledForm("value.hour").value.value mustEqual time.hour.toString
      filledForm("value.minute").value.value mustEqual time.minute.toString
      filledForm("value.ampm").value.value mustEqual time.amOrPm
    }
  }
}
