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

import javax.inject.Inject
import forms.mappings.Mappings
import models.WhatTimeDidYouFinish
import play.api.data.Form
import play.api.data.validation._

class WhatTimeDidYouFinishFormProvider @Inject() extends Mappings {

  def apply(): Form[WhatTimeDidYouFinish] =
    Form(
      "value" -> text("whatTimeDidYouFinish.error.required")
        .verifying(formatConstraint)
    )

  private val validSuffixes = List("am", "a.m", "a.m.", "pm", "p.m", "p.m.")

  private val formatConstraint: Constraint[String] = Constraint {
    case value if !validSuffixes.exists(value.toLowerCase.endsWith(_)) =>
      Invalid("whatTimeDidYouFinish.error.morningOrAfternoon", value)
    case value if value.matches("^(?i)([0-9]|0[0-9]|1[0-2])([:\\. ][0-5][0-9])? ?((a|p)\\.?m\\.?)$") =>
      Valid
    case value =>
      Invalid("whatTimeDidYouFinish.error.format", value)
  }

}
