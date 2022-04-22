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

package navigation

import base.SpecBase
import controllers.routes
import pages._
import models._

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, NormalMode, emptyUserAnswers) mustBe routes.IndexController.onPageLoad
      }

      "must go from the what is your name page to the what is your nino page" in {
        navigator.nextPage(WhatIsYourNamePage, NormalMode, emptyUserAnswers) mustBe routes.WhatIsYourNinoController.onPageLoad(NormalMode)
      }

      "must go from the what is your nino page to the what is your date of birth page" in {
        navigator.nextPage(WhatIsYourNinoPage, NormalMode, emptyUserAnswers) mustBe routes.WhatIsYourDateOfBirthController.onPageLoad(NormalMode)
      }

      "must go from the what is your date of birth page to the do you know your clock or payroll page" in {
        navigator.nextPage(WhatIsYourDateOfBirthPage, NormalMode, emptyUserAnswers) mustBe routes.DoYouKnowYourClockOrPayrollNumberController.onPageLoad(NormalMode)
      }

      "must go from the do you know your clock or payroll page" - {

        "to the what is your clock or payroll number page when the user selects yes" in {
          val answers = emptyUserAnswers.set(DoYouKnowYourClockOrPayrollNumberPage, true).success.value
          navigator.nextPage(DoYouKnowYourClockOrPayrollNumberPage, NormalMode, answers) mustBe routes.WhatIsYourClockOrPayrollNumberController.onPageLoad(NormalMode)
        }

        "to the details of sickness page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouKnowYourClockOrPayrollNumberPage, false).success.value
          navigator.nextPage(DoYouKnowYourClockOrPayrollNumberPage, NormalMode, answers) mustBe routes.DetailsOfSicknessController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no answer" in {
          navigator.nextPage(DoYouKnowYourClockOrPayrollNumberPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the what is your clock or payroll number to the details of sickness page" in {
        navigator.nextPage(WhatIsYourClockOrPayrollNumberPage, NormalMode, emptyUserAnswers) mustBe routes.DetailsOfSicknessController.onPageLoad(NormalMode)
      }

      "must go from the what is your clock or payroll number to the date sickness began page" in {
        navigator.nextPage(DetailsOfSicknessPage, NormalMode, emptyUserAnswers) mustBe routes.DateSicknessBeganController.onPageLoad(NormalMode)
      }

      "must go from the date sickness began page to the has sickness ended page" in {
        navigator.nextPage(DateSicknessBeganPage, NormalMode, emptyUserAnswers) mustBe routes.HasSicknessEndedController.onPageLoad(NormalMode)
      }

      "must go from the has sickness ended page" - {

        "to the date sickness ended page when the user selects yes" in {
          val answers = emptyUserAnswers.set(HasSicknessEndedPage, true).success.value
          navigator.nextPage(HasSicknessEndedPage, NormalMode, answers) mustBe routes.DateSicknessEndedController.onPageLoad(NormalMode)
        }

        "to the when did you last work page when the user selects no" in {
          val answers = emptyUserAnswers.set(HasSicknessEndedPage, false).success.value
          navigator.nextPage(HasSicknessEndedPage, NormalMode, answers) mustBe routes.WhenDidYouLastWorkController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no answer" in {
          navigator.nextPage(HasSicknessEndedPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the date sickness ended page to the when did you last work page" in {
        navigator.nextPage(DateSicknessEndedPage, NormalMode, emptyUserAnswers) mustBe routes.WhenDidYouLastWorkController.onPageLoad(NormalMode)
      }

      "must go from the when did you last work page to the what time did you finish on that date page" in {
        navigator.nextPage(WhenDidYouLastWorkPage, NormalMode, emptyUserAnswers) mustBe routes.WhatTimeDidYouFinishController.onPageLoad(NormalMode)
      }

      "must go from the what time did you finish page to the caused by accident or industrial disease page" in {
        navigator.nextPage(WhatTimeDidYouFinishPage, NormalMode, emptyUserAnswers) mustBe routes.CausedByAccidentOrIndustrialDiseaseController.onPageLoad(NormalMode)
      }

      "must go from the caused by accident or industrial disease page" - {

        "to the guidance page when the user selects yes" ignore {
          // TODO
        }

        "to the phone number page when the user selects no" in {
          val answers = emptyUserAnswers.set(CausedByAccidentOrIndustrialDiseasePage, false).success.value
          navigator.nextPage(CausedByAccidentOrIndustrialDiseasePage, NormalMode, answers) mustBe routes.PhoneNumberController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when there are no user answers" in {
          navigator.nextPage(CausedByAccidentOrIndustrialDiseasePage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the phone number page to the check your answers page" in {
        navigator.nextPage(PhoneNumberPage, NormalMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }
  }
}
