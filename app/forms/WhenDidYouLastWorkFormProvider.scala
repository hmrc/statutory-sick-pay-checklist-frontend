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

import java.time.{Clock, LocalDate}
import forms.mappings.Mappings

import javax.inject.Inject
import play.api.data.Form

import java.time.format.DateTimeFormatter

class WhenDidYouLastWorkFormProvider @Inject()(clock: Clock) extends Mappings {

  def minDate: LocalDate = LocalDate.now(clock).minusYears(1)

  private def dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def apply(startDate: LocalDate): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey     = "whenDidYouLastWork.error.invalid",
        allRequiredKey = "whenDidYouLastWork.error.required.all",
        twoRequiredKey = "whenDidYouLastWork.error.required.two",
        requiredKey    = "whenDidYouLastWork.error.required"
      ).verifying(
        maxDate(startDate, "whenDidYouLastWork.error.afterMaximum", startDate.format(dateFormatter)),
        minDate(minDate, "whenDidYouLastWork.error.beforeMinimum", minDate.format(dateFormatter))
      )
    )
}
