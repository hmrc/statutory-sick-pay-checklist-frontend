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

package forms

import forms.behaviours.{IntFieldBehaviours, StringFieldBehaviours}
import org.scalacheck.{Arbitrary, Gen, Shrink}
import play.api.data.FormError

class WhatTimeDidYouFinishFormProviderSpec extends StringFieldBehaviours with IntFieldBehaviours {

  val form = new WhatTimeDidYouFinishFormProvider()()

  ".time-finished-hour" - {

    val fieldName = "time-finished-hour"
    val requiredKey = "whatTimeDidYouFinish.time-finished-hour.error.required"
    val rangeKey = "whatTimeDidYouFinish.time-finished-hour.error.range"
    val nonNumericKey = "whatTimeDidYouFinish.time-finished-hour.error.numeric"
    val wholeNumberKey = "whatTimeDidYouFinish.time-finished-hour.error.wholeNumber"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      Gen.oneOf(List("9", "09", "12"))
    )

    behave like intField(
      form,
      fieldName,
      FormError(fieldName, nonNumericKey),
      FormError(fieldName, wholeNumberKey)
    )

    behave like intFieldWithRange(
      form,
      fieldName,
      1,
      12,
      FormError(fieldName, rangeKey, Seq(1, 12))
    )

    behave like mandatoryField(
      form,
      fieldName,
      FormError(fieldName, requiredKey)
    )
  }

  ".time-finished-minute" - {

    val fieldName = "time-finished-minute"
    val requiredKey = "whatTimeDidYouFinish.time-finished-minute.error.required"
    val rangeKey = "whatTimeDidYouFinish.time-finished-minute.error.range"
    val nonNumericKey = "whatTimeDidYouFinish.time-finished-minute.error.numeric"
    val wholeNumberKey = "whatTimeDidYouFinish.time-finished-minute.error.wholeNumber"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      Gen.oneOf(List("0", "09", "9", "00"))
    )

    behave like intField(
      form,
      fieldName,
      FormError(fieldName, nonNumericKey),
      FormError(fieldName, wholeNumberKey)
    )

    behave like intFieldWithRange(
      form,
      fieldName,
      0,
      59,
      FormError(fieldName, rangeKey, Seq(0, 59))
    )

    behave like mandatoryField(
      form,
      fieldName,
      FormError(fieldName, requiredKey)
    )
  }

  ".time-finished-ampm" - {

    val fieldName = "time-finished-ampm"
    val requiredKey = "whatTimeDidYouFinish.time-finished-ampm.error.required"
    val invalidKey = "whatTimeDidYouFinish.time-finished-ampm.error.invalid"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      Gen.oneOf(List("am", "pm"))
    )

    behave like mandatoryField(
      form,
      fieldName,
      FormError(fieldName, requiredKey)
    )

    "must fail to bind value that is not am or pm" in {

      val gen = nonEmptyString
        .suchThat(_ !== "am")
        .suchThat(_ !== "pm")

      forAll(gen) { value =>
        val errors = form.bind(Map(fieldName -> value))(fieldName).errors
        errors must contain (FormError(fieldName, invalidKey))
      }
    }
  }
}
