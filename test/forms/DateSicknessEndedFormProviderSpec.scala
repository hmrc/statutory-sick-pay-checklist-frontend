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

import java.time.{LocalDate, ZoneOffset}
import forms.behaviours.DateBehaviours
import play.api.data.FormError

import java.time.format.DateTimeFormatter

class DateSicknessEndedFormProviderSpec extends DateBehaviours {

  private val startDate = LocalDate.now().minusDays(5)
  val form = new DateSicknessEndedFormProvider()(startDate)

  private def dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  ".value" - {

    val validData = datesBetween(
      min = LocalDate.now().minusDays(5),
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(form, "value", validData)

    behave like mandatoryDateField(form, "value", "dateSicknessEnded.error.required.all")

    behave like dateFieldWithMax(form, "value", LocalDate.now, FormError("value", "dateSicknessEnded.error.afterMaximum", Seq(LocalDate.now().format(dateFormatter))))

    behave like dateFieldWithMin(form, "value", LocalDate.now().minusDays(5), FormError("value", "dateSicknessEnded.error.beforeMinimum", Seq(startDate.format(dateFormatter))))
  }
}
