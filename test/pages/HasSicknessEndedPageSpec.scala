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

package pages

import models.UserAnswers
import pages.behaviours.PageBehaviours

import java.time.LocalDate

class HasSicknessEndedPageSpec extends PageBehaviours {

  "HasSicknessEndedPage" - {

    beRetrievable[Boolean](HasSicknessEndedPage)

    beSettable[Boolean](HasSicknessEndedPage)

    beRemovable[Boolean](HasSicknessEndedPage)

    "must remove when did your sickness end when set to no" in {
      val date = LocalDate.now
      val answers = UserAnswers("id")
        .set(DateSicknessEndedPage, date).success.value
        .set(HasSicknessEndedPage, false).success.value
      answers.get(DateSicknessEndedPage) mustBe empty
    }

    "must not remove when did your sickness end when set to true" in {
      val date = LocalDate.now
      val answers = UserAnswers("id")
        .set(DateSicknessEndedPage, date).success.value
        .set(HasSicknessEndedPage, true).success.value
      answers.get(DateSicknessEndedPage).value mustBe date
    }
  }
}
