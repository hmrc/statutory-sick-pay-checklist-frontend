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

package controllers

import base.SpecBase
import models.{WhatIsYourName, WhatTimeDidYouFinish}
import pages._
import play.api.i18n.Messages
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.domain.Nino
import viewmodels.checkAnswers._
import viewmodels.govuk.SummaryListFluency
import views.html.CheckYourAnswersView

import java.time.LocalDate

class CheckYourAnswersControllerSpec extends SpecBase with SummaryListFluency {

  "Check Your Answers Controller" - {

    "must return OK and the correct view for a GET" in {

      val answers = emptyUserAnswers
        .set(WhatIsYourNamePage, WhatIsYourName("first", "last")).success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
        .set(WhatIsYourNinoPage, Nino("AA123456A")).success.value
        .set(WhatIsYourDateOfBirthPage, LocalDate.of(2000, 2, 1)).success.value
        .set(PhoneNumberPage, "07875242851").success.value
        .set(DetailsOfSicknessPage, "some details").success.value
        .set(DateSicknessBeganPage, LocalDate.of(2001, 2, 1)).success.value
        .set(HasSicknessEndedPage, true).success.value
        .set(DateSicknessEndedPage, LocalDate.of(2002, 2, 1)).success.value
        .set(CausedByAccidentOrIndustrialDiseasePage, true).success.value
        .set(WhenDidYouLastWorkPage, LocalDate.of(2003, 2, 1)).success.value
        .set(WhatTimeDidYouFinishPage, WhatTimeDidYouFinish(9, 0, "am")).success.value
        .set(DoYouKnowYourClockOrPayrollNumberPage, true).success.value
        .set(WhatIsYourClockOrPayrollNumberPage, "prcn").success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      implicit val m: Messages = messages(application)

      val personalDetails = SummaryListViewModel(
        Seq(
          WhatIsYourNameSummary.row(answers),
          DoYouKnowYourNationalInsuranceNumberSummary.row(answers),
          WhatIsYourNinoSummary.row(answers),
          WhatIsYourDateOfBirthSummary.row(answers),
          PhoneNumberSummary.row(answers)
        ).flatten
      )

      val sicknessDetails = SummaryListViewModel(
        Seq(
          DetailsOfSicknessSummary.row(answers),
          DateSicknessBeganSummary.row(answers),
          HasSicknessEndedSummary.row(answers),
          DateSicknessEndedSummary.row(answers),
          CausedByAccidentOrIndustrialDiseaseSummary.row(answers)
        ).flatten
      )

      val employmentDetails = SummaryListViewModel(
        Seq(
          WhenDidYouLastWorkSummary.row(answers),
          WhatTimeDidYouFinishSummary.row(answers),
          DoYouKnowYourClockOrPayrollNumberSummary.row(answers),
          WhatIsYourClockOrPayrollNumberSummary.row(answers)
        ).flatten
      )

      running(application) {
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[CheckYourAnswersView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(personalDetails, sicknessDetails, employmentDetails)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
