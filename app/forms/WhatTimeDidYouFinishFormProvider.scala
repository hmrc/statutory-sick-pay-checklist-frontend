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

import forms.mappings.Mappings
import models.WhatTimeDidYouFinish
import play.api.data.Form
import play.api.data.Forms.mapping

import javax.inject.Inject

class WhatTimeDidYouFinishFormProvider @Inject() extends Mappings {

  def apply(): Form[WhatTimeDidYouFinish] = Form(
    mapping(
      "time-finished-hour" -> int(
        "whatTimeDidYouFinish.time-finished-hour.error.required",
     "whatTimeDidYouFinish.time-finished-hour.error.wholeNumber",
     "whatTimeDidYouFinish.time-finished-hour.error.numeric"
    )
  .verifying(inRange(1, 12, "whatTimeDidYouFinish.time-finished-hour.error.range")),
      "time-finished-minute" -> int(
        "whatTimeDidYouFinish.time-finished-minute.error.required",
    "whatTimeDidYouFinish.time-finished-minute.error.wholeNumber",
    "whatTimeDidYouFinish.time-finished-minute.error.numeric"
      )
  .verifying(inRange(0, 59, "whatTimeDidYouFinish.time-finished-minute.error.range")),
    "time-finished-ampm" -> text("whatTimeDidYouFinish.time-finished-ampm.error.required")
      .verifying("whatTimeDidYouFinish.time-finished-ampm.error.invalid", List("am", "pm").contains(_))
    )(WhatTimeDidYouFinish.apply)(WhatTimeDidYouFinish.unapply)
  )
}
