/*
 * Copyright 2024 HM Revenue & Customs
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

import java.time.LocalDate
import play.api.libs.json.JsPath

import scala.util.{Success, Try}

case object DateSicknessBeganPage extends QuestionPage[LocalDate] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "dateSicknessBegan"

  override def cleanup(value: Option[LocalDate], userAnswers: UserAnswers): Try[UserAnswers] = {

    def cleanupEndDate(userAnswers: UserAnswers): Try[UserAnswers] = {
      for {
        startDate <- value
        endDate   <- userAnswers.get(DateSicknessEndedPage)
      } yield if (endDate isBefore startDate) userAnswers.remove(DateSicknessEndedPage) else Success(userAnswers)
    }.getOrElse(userAnswers.remove(DateSicknessEndedPage))

    def cleanupLastWorkedDate(userAnswers: UserAnswers): Try[UserAnswers] = {
      for {
        startDate <- value
        lastWorked <- userAnswers.get(WhenDidYouLastWorkPage)
      } yield if (!(lastWorked isBefore startDate)) userAnswers.remove(WhenDidYouLastWorkPage) else Success(userAnswers)
    }.getOrElse(userAnswers.remove(WhenDidYouLastWorkPage))

    cleanupEndDate(userAnswers).flatMap(cleanupLastWorkedDate)
  }
}
