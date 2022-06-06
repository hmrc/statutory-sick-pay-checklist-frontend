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

import javax.inject.{Inject, Singleton}

import play.api.mvc.Call
import controllers.routes
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case WhatIsYourNamePage => _ => routes.DoYouKnowYourNationalInsuranceNumberController.onPageLoad(NormalMode)
    case DoYouKnowYourNationalInsuranceNumberPage => doYouKnowYourNationalInsuranceNumberRoutes
    case WhatIsYourNinoPage => _ => routes.WhatIsYourDateOfBirthController.onPageLoad(NormalMode)
    case WhatIsYourDateOfBirthPage => _ => routes.PhoneNumberController.onPageLoad(NormalMode)
    case PhoneNumberPage => _ => routes.DetailsOfSicknessController.onPageLoad(NormalMode)
    case DetailsOfSicknessPage => _ => routes.DateSicknessBeganController.onPageLoad(NormalMode)
    case DateSicknessBeganPage => _ => routes.HasSicknessEndedController.onPageLoad(NormalMode)
    case HasSicknessEndedPage => hasSicknessEndedRoutes
    case DateSicknessEndedPage => _ => routes.CausedByAccidentOrIndustrialDiseaseController.onPageLoad(NormalMode)
    case CausedByAccidentOrIndustrialDiseasePage => _ => routes.WhenDidYouLastWorkController.onPageLoad(NormalMode)
    case WhenDidYouLastWorkPage => _ => routes.WhatTimeDidYouFinishController.onPageLoad(NormalMode)
    case WhatTimeDidYouFinishPage => _ => routes.CheckYourAnswersController.onPageLoad
    case DoYouKnowYourClockOrPayrollNumberPage => doYouKnowYourClockOrPayrollNumberRoutes
    case WhatIsYourClockOrPayrollNumberPage => _ => routes.CheckYourAnswersController.onPageLoad
    case _ => _ => routes.IndexController.onPageLoad
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    case DoYouKnowYourNationalInsuranceNumberPage => doYouKnowYourNationalInsuranceNumberCheckRoutes
    case HasSicknessEndedPage => hasSicknessEndedCheckRoutes
    case DoYouKnowYourClockOrPayrollNumberPage => doYouKnowYourClockOrPayrollNumberCheckRoutes
    case _ => _ => routes.CheckYourAnswersController.onPageLoad
  }

  private def doYouKnowYourNationalInsuranceNumberRoutes(answers: UserAnswers): Call =
    answers.get(DoYouKnowYourNationalInsuranceNumberPage).map {
      case true => routes.WhatIsYourNinoController.onPageLoad(NormalMode)
      case false => routes.WhatIsYourDateOfBirthController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouKnowYourNationalInsuranceNumberCheckRoutes(answers: UserAnswers): Call = {
    (answers.get(DoYouKnowYourNationalInsuranceNumberPage), answers.get(WhatIsYourNinoPage)) match {
      case (Some(false), _)       => routes.CheckYourAnswersController.onPageLoad
      case (Some(true),  Some(_)) => routes.CheckYourAnswersController.onPageLoad
      case (Some(true),  None)    => routes.WhatIsYourNinoController.onPageLoad(CheckMode)
      case (_, _)                 => routes.JourneyRecoveryController.onPageLoad()
    }
  }

  private def doYouKnowYourClockOrPayrollNumberRoutes(answers: UserAnswers): Call =
    answers.get(DoYouKnowYourClockOrPayrollNumberPage).map {
      case true  => routes.WhatIsYourClockOrPayrollNumberController.onPageLoad(NormalMode)
      case false => routes.CheckYourAnswersController.onPageLoad
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouKnowYourClockOrPayrollNumberCheckRoutes(answers: UserAnswers): Call =
    answers.get(DoYouKnowYourClockOrPayrollNumberPage).map {
      case true  => routes.WhatIsYourClockOrPayrollNumberController.onPageLoad(CheckMode)
      case false => routes.CheckYourAnswersController.onPageLoad
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def hasSicknessEndedRoutes(answers: UserAnswers): Call =
    answers.get(HasSicknessEndedPage).map {
      case true  => routes.DateSicknessEndedController.onPageLoad(NormalMode)
      case false => routes.CausedByAccidentOrIndustrialDiseaseController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def hasSicknessEndedCheckRoutes(answers: UserAnswers): Call =
    answers.get(HasSicknessEndedPage).map {
      case true  => routes.DateSicknessEndedController.onPageLoad(CheckMode)
      case false => routes.CheckYourAnswersController.onPageLoad
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
