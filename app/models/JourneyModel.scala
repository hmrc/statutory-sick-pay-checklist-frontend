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

import cats.data.EitherNec
import cats.implicits._
import pages._
import queries.Query
import uk.gov.hmrc.domain.Nino

import java.time.LocalDate

final case class JourneyModel(
                               firstName: String,
                               surname: String,
                               nino: Option[String],
                               dob: LocalDate,
                               telephoneNumber: String,
                               payrollOrClockNumber: Option[String],
                               sicknessDetails: String,
                               sicknessStartDate: LocalDate,
                               sicknessEndDate: Option[LocalDate],
                               whenDidYouLastWork: LocalDate,
                               whatTimeDidYouFinish: WhatTimeDidYouFinish,
                               causedByIndustrialDiseaseOrAccident: Boolean
                             )

object JourneyModel {

  def from(answers: UserAnswers): EitherNec[Query, JourneyModel] = {
    (
      answers.getEither(WhatIsYourNamePage),
      getNino(answers),
      answers.getEither(WhatIsYourDateOfBirthPage),
      answers.getEither(PhoneNumberPage),
      getClockOrPayrollNumber(answers),
      answers.getEither(DetailsOfSicknessPage),
      answers.getEither(DateSicknessBeganPage),
      getDateSicknessEnded(answers),
      answers.getEither(WhenDidYouLastWorkPage),
      answers.getEither(WhatTimeDidYouFinishPage),
      answers.getEither(CausedByAccidentOrIndustrialDiseasePage)
    ).parMapN { (names, nino, dob, phone, prcn, details, startDate, endDate, lastWorkingDate, time, accidentOrIndustrialDisease) =>
      JourneyModel(
        names.firstname,
        names.surname,
        nino.map(_.value),
        dob,
        phone,
        prcn,
        details,
        startDate,
        endDate,
        lastWorkingDate,
        time,
        accidentOrIndustrialDisease
      )
    }
  }

  private def getNino(answers: UserAnswers): EitherNec[Query, Option[Nino]] =
    answers.getEither(DoYouKnowYourNationalInsuranceNumberPage).flatMap {
      case true  => answers.getEither(WhatIsYourNinoPage).map(Some(_))
      case false => Right(None)
    }

  private def getClockOrPayrollNumber(answers: UserAnswers): EitherNec[Query, Option[String]] =
    answers.getEither(DoYouKnowYourClockOrPayrollNumberPage).flatMap {
      case true  => answers.getEither(WhatIsYourClockOrPayrollNumberPage).map(Some(_))
      case false => Right(None)
    }

  private def getDateSicknessEnded(answers: UserAnswers): EitherNec[Query, Option[LocalDate]] =
    answers.getEither(HasSicknessEndedPage).flatMap {
      case true  => answers.getEither(DateSicknessEndedPage).map(Some(_))
      case false => Right(None)
    }
}