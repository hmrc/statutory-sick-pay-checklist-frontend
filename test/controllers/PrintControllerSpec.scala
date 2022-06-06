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
import models.{JourneyModel, WhatIsYourName}
import org.scalatest.EitherValues
import pages.{CausedByAccidentOrIndustrialDiseasePage, DateSicknessBeganPage, DateSicknessEndedPage, DetailsOfSicknessPage, DoYouKnowYourClockOrPayrollNumberPage, DoYouKnowYourNationalInsuranceNumberPage, HasSicknessEndedPage, PhoneNumberPage, WhatIsYourClockOrPayrollNumberPage, WhatIsYourDateOfBirthPage, WhatIsYourNamePage, WhatIsYourNinoPage, WhatTimeDidYouFinishPage, WhenDidYouLastWorkPage}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.domain.Nino
import views.html.PrintView

import java.time.LocalDate

class PrintControllerSpec extends SpecBase with EitherValues {

  "Print Controller" - {

    "must return OK and the correct view for a GET" in {

      val answers = emptyUserAnswers
        .set(WhatIsYourNamePage, WhatIsYourName("first", "last")).success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
        .set(WhatIsYourNinoPage, Nino("AA123456A")).success.value
        .set(WhatIsYourDateOfBirthPage, LocalDate.of(2000, 2, 1)).success.value
        .set(PhoneNumberPage, "07875242851").success.value
        .set(DoYouKnowYourClockOrPayrollNumberPage, true).success.value
        .set(WhatIsYourClockOrPayrollNumberPage, "prcn").success.value
        .set(DetailsOfSicknessPage, "some details").success.value
        .set(DateSicknessBeganPage, LocalDate.of(2001, 2, 1)).success.value
        .set(HasSicknessEndedPage, true).success.value
        .set(DateSicknessEndedPage, LocalDate.of(2002, 2, 1)).success.value
        .set(CausedByAccidentOrIndustrialDiseasePage, true).success.value
        .set(WhenDidYouLastWorkPage, LocalDate.of(2003, 2, 1)).success.value
        .set(WhatTimeDidYouFinishPage, "9am").success.value

      val model = JourneyModel.from(answers).right.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onPageLoad().url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[PrintView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(model)(request, messages(application)).toString
      }
    }
  }
}
