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

import org.scalatest.{EitherValues, OptionValues, TryValues}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import pages._
import uk.gov.hmrc.domain.Nino

import java.time.LocalDate

class JourneyModelSpec extends AnyFreeSpec with Matchers with OptionValues with EitherValues with TryValues {

  "from" - {

    "must return a completed journey model when the user has all answers" in {

      val answers: UserAnswers = UserAnswers("id")
        .set(WhatIsYourNamePage, WhatIsYourName("first", "surname")).success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
        .set(WhatIsYourNinoPage, Nino("AA123456A")).success.value
        .set(WhatIsYourDateOfBirthPage, LocalDate.of(1990, 2, 1)).success.value
        .set(PhoneNumberPage, "tel").success.value
        .set(DoYouKnowYourClockOrPayrollNumberPage, true).success.value
        .set(WhatIsYourClockOrPayrollNumberPage, "prcn").success.value
        .set(DetailsOfSicknessPage, "some details").success.value
        .set(DateSicknessBeganPage, LocalDate.of(2000, 3, 2)).success.value
        .set(HasSicknessEndedPage, true).success.value
        .set(DateSicknessEndedPage, LocalDate.of(2000, 4, 3)).success.value
        .set(WhenDidYouLastWorkPage, LocalDate.of(2000, 2, 1)).success.value
        .set(WhatTimeDidYouFinishPage, "12:00pm").success.value
        .set(CausedByAccidentOrIndustrialDiseasePage, true).success.value

      val expected = JourneyModel(
        firstName = "first",
        surname = "surname",
        nino = Some("AA123456A"),
        dob = LocalDate.of(1990, 2, 1),
        telephoneNumber = "tel",
        payrollOrClockNumber = Some("prcn"),
        sicknessDetails = "some details",
        sicknessStartDate = LocalDate.of(2000, 3, 2),
        sicknessEndDate = Some(LocalDate.of(2000, 4, 3)),
        whenDidYouLastWork = LocalDate.of(2000, 2, 1),
        whatTimeDidYouFinish = "12:00pm",
        causedByIndustrialDiseaseOrAccident = true
      )

      JourneyModel.from(answers).toOption.value mustEqual expected
    }

    "must return a completed journey model when the user has minimum answers" in {

      val answers: UserAnswers = UserAnswers("id")
        .set(WhatIsYourNamePage, WhatIsYourName("first", "surname")).success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, false).success.value
        .set(WhatIsYourDateOfBirthPage, LocalDate.of(1990, 2, 1)).success.value
        .set(PhoneNumberPage, "tel").success.value
        .set(DoYouKnowYourClockOrPayrollNumberPage, false).success.value
        .set(DetailsOfSicknessPage, "some details").success.value
        .set(DateSicknessBeganPage, LocalDate.of(2000, 3, 2)).success.value
        .set(HasSicknessEndedPage, false).success.value
        .set(WhenDidYouLastWorkPage, LocalDate.of(2000, 2, 1)).success.value
        .set(WhatTimeDidYouFinishPage, "12:00pm").success.value
        .set(CausedByAccidentOrIndustrialDiseasePage, true).success.value

      val expected = JourneyModel(
        firstName = "first",
        surname = "surname",
        nino = None,
        dob = LocalDate.of(1990, 2, 1),
        telephoneNumber = "tel",
        payrollOrClockNumber = None,
        sicknessDetails = "some details",
        sicknessStartDate = LocalDate.of(2000, 3, 2),
        sicknessEndDate = None,
        whenDidYouLastWork = LocalDate.of(2000, 2, 1),
        whatTimeDidYouFinish = "12:00pm",
        causedByIndustrialDiseaseOrAccident = true
      )

      JourneyModel.from(answers).toOption.value mustEqual expected
    }

    "must return all the pages which failed" in {

      val errors = JourneyModel.from(UserAnswers("id")).left.value.toChain.toList

      errors must contain only(
        WhatIsYourNamePage,
        DoYouKnowYourNationalInsuranceNumberPage,
        WhatIsYourDateOfBirthPage,
        PhoneNumberPage,
        DoYouKnowYourClockOrPayrollNumberPage,
        DetailsOfSicknessPage,
        DateSicknessBeganPage,
        HasSicknessEndedPage,
        WhenDidYouLastWorkPage,
        WhatTimeDidYouFinishPage,
        CausedByAccidentOrIndustrialDiseasePage
      )
    }
  }
}
