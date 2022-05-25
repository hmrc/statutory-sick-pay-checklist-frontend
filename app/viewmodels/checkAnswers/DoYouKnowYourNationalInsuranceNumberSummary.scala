package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.DoYouKnowYourNationalInsuranceNumberPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object DoYouKnowYourNationalInsuranceNumberSummary  {

  def row(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(DoYouKnowYourNationalInsuranceNumberPage).map {
      answer =>

        val value = if (answer) "site.yes" else "site.no"

        SummaryListRowViewModel(
          key     = "doYouKnowYourNationalInsuranceNumber.checkYourAnswersLabel",
          value   = ValueViewModel(value),
          actions = Seq(
            ActionItemViewModel("site.change", routes.DoYouKnowYourNationalInsuranceNumberController.onPageLoad(CheckMode).url)
              .withVisuallyHiddenText(messages("doYouKnowYourNationalInsuranceNumber.change.hidden"))
          )
        )
    }
}
