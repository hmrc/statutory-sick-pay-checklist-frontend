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

import com.google.inject.Inject
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.{DoYouKnowYourClockOrPayrollNumberSummary, PhoneNumberSummary, _}
import viewmodels.govuk.summarylist._
import views.html.CheckYourAnswersView

class CheckYourAnswersController @Inject()(
                                            override val messagesApi: MessagesApi,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: CheckYourAnswersView
                                          ) extends FrontendBaseController with I18nSupport {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val answers = request.userAnswers

      val personalDetails = SummaryListViewModel(
        rows = Seq(
          WhatIsYourNameSummary.row(answers),
          DoYouKnowYourNationalInsuranceNumberSummary.row(answers),
          WhatIsYourNinoSummary.row(answers),
          WhatIsYourDateOfBirthSummary.row(answers),
          PhoneNumberSummary.row(answers)
        ).flatten
      )

      val sicknessDetails = SummaryListViewModel(
        rows = Seq(
          DetailsOfSicknessSummary.row(answers),
          DateSicknessBeganSummary.row(answers),
          HasSicknessEndedSummary.row(answers),
          DateSicknessEndedSummary.row(answers),
          CausedByAccidentOrIndustrialDiseaseSummary.row(answers)
        ).flatten
      )

      val employmentDetails = SummaryListViewModel(
        rows = Seq(
          WhenDidYouLastWorkSummary.row(answers),
          WhatTimeDidYouFinishSummary.row(answers),
          DoYouKnowYourClockOrPayrollNumberSummary.row(answers),
          WhatIsYourClockOrPayrollNumberSummary.row(answers)
        ).flatten
      )

      Ok(view(personalDetails, sicknessDetails, employmentDetails))
  }
}
