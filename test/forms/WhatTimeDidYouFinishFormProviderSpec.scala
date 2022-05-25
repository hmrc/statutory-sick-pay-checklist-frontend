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

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class WhatTimeDidYouFinishFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "whatTimeDidYouFinish.error.required"
  val morningOrAfternoonErrorKey = "whatTimeDidYouFinish.error.morningOrAfternoon"
  val formatErrorKey = "whatTimeDidYouFinish.error.format"

  val form = new WhatTimeDidYouFinishFormProvider()()

  ".value" - {

    val fieldName = "value"

    "must bind valid values" in {
      val validCases = List("9am", "9:30am", "12:15pm", "6 pm", "9.15pm", "3 20am", "09:29am", "11 59 pm", "0:12am", "00:39pm", "12:00am", "11:01pm", "12Am", "12PM", "12:01 aM")
      validCases.foreach {
        validCase =>
          val result = form.bind(Map(fieldName -> validCase)).apply(fieldName)
          result.value.value mustBe validCase
          result.errors mustBe empty
      }
    }

    "must not bind values missing am/pm" in {
      val missingAmPm = List("9", "10", "11:00", "12:15", "1 00", "10.30")
      missingAmPm.foreach {
        invalidCase =>
          val result = form.bind(Map(fieldName -> invalidCase)).apply(fieldName)
          result.errors mustBe Seq(FormError("value", morningOrAfternoonErrorKey))
      }
    }

    "must not bind values in the wrong format" in {
      val invalidFormat = List("23:00am", "am", "pm", "21am", "21:00pm", "20am", "1234pm", "aa:bbam")
      invalidFormat.foreach {
        invalidCase =>
          val result = form.bind(Map(fieldName -> invalidCase)).apply(fieldName)
          result.errors mustBe Seq(FormError("value", formatErrorKey))
      }
    }

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

  }
}
