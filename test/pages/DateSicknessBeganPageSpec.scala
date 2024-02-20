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
import org.scalacheck.Arbitrary
import org.scalatest.TryValues
import pages.behaviours.PageBehaviours

class DateSicknessBeganPageSpec extends PageBehaviours with TryValues {

  "DateSicknessBeganPage" - {

    implicit lazy val arbitraryLocalDate: Arbitrary[LocalDate] = Arbitrary {
      datesBetween(LocalDate.of(1900, 1, 1), LocalDate.of(2100, 1, 1))
    }

    beRetrievable[LocalDate](DateSicknessBeganPage)

    beSettable[LocalDate](DateSicknessBeganPage)

    beRemovable[LocalDate](DateSicknessBeganPage)

    "when DateSicknessEnded is before DateSicknessBegan" - {

      "and WhenDidYouLastWork is before DateSicknessBegan" - {

        "must remove DateSicknessEnded and keep WhenDidYouLastWork" in {
          val answers = UserAnswers("id")
            .set(DateSicknessEndedPage, LocalDate.of(2022, 2, 1)).success.value
            .set(WhenDidYouLastWorkPage, LocalDate.of(2022, 2, 1)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(DateSicknessEndedPage) mustBe empty
          answers.get(WhenDidYouLastWorkPage).value mustBe LocalDate.of(2022, 2, 1)
        }

      }

      "and WhenDidYouLastWork is on DateSicknessBegan" - {

        "must remove DateSicknessEnded and remove WhenDidYouLastWork" in {
          val answers = UserAnswers("id")
            .set(DateSicknessEndedPage, LocalDate.of(2022, 2, 1)).success.value
            .set(WhenDidYouLastWorkPage, LocalDate.of(2022, 2, 2)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(DateSicknessEndedPage) mustBe empty
          answers.get(WhenDidYouLastWorkPage) mustBe empty
        }

      }

      "and WhenDidYouLastWork is after DateSicknessBegan" - {

        "must remove DateSicknessEnded and remove WhenDidYouLastWork" in {
          val answers = UserAnswers("id")
            .set(DateSicknessEndedPage, LocalDate.of(2022, 2, 1)).success.value
            .set(WhenDidYouLastWorkPage, LocalDate.of(2022, 2, 3)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(DateSicknessEndedPage) mustBe empty
          answers.get(WhenDidYouLastWorkPage) mustBe empty
        }

      }

      "and WhenDidYouLastWork is not set" - {

        "must remove DateSicknessEnded" in {
          val answers = UserAnswers("id")
            .set(DateSicknessEndedPage, LocalDate.of(2022, 2, 1)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(DateSicknessEndedPage) mustBe empty
          answers.get(WhenDidYouLastWorkPage) mustBe empty
        }
      }

    }

    "when DateSicknessEnded is after DateSicknessBegan" - {

      "and WhenDidYouLastWork is before DateSicknessBegan" - {

        "must keep DateSicknessEnded and keep WhenDidYouLastWork" in {
          val answers = UserAnswers("id")
            .set(DateSicknessEndedPage, LocalDate.of(2022, 2, 3)).success.value
            .set(WhenDidYouLastWorkPage, LocalDate.of(2022, 2, 1)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(DateSicknessEndedPage).value mustBe LocalDate.of(2022, 2, 3)
          answers.get(WhenDidYouLastWorkPage).value mustBe LocalDate.of(2022, 2, 1)
        }

      }

      "and WhenDidYouLastWork is on DateSicknessBegan" - {

        "must keep DateSicknessEnded and remove WhenDidYouLastWork" in {
          val answers = UserAnswers("id")
            .set(DateSicknessEndedPage, LocalDate.of(2022, 2, 3)).success.value
            .set(WhenDidYouLastWorkPage, LocalDate.of(2022, 2, 2)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(DateSicknessEndedPage).value mustBe LocalDate.of(2022, 2, 3)
          answers.get(WhenDidYouLastWorkPage) mustBe empty
        }

      }

      "and WhenDidYouLastWork is after DateSicknessBegan" - {

        "must keep DateSicknessEnded and remove WhenDidYouLastWork" in {
          val answers = UserAnswers("id")
            .set(DateSicknessEndedPage, LocalDate.of(2022, 2, 3)).success.value
            .set(WhenDidYouLastWorkPage, LocalDate.of(2022, 2, 3)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(DateSicknessEndedPage).value mustBe LocalDate.of(2022, 2, 3)
          answers.get(WhenDidYouLastWorkPage) mustBe empty
        }
      }

      "and WhenDidYouLastWork is not set" - {

        "must keep DateSicknessEnded" in {
          val answers = UserAnswers("id")
            .set(DateSicknessEndedPage, LocalDate.of(2022, 2, 3)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(DateSicknessEndedPage).value mustBe LocalDate.of(2022, 2, 3)
          answers.get(WhenDidYouLastWorkPage) mustBe empty
        }
      }

    }

    "when DateSicknessEnded is not set" - {

      "and WhenDidYouLastWork is before DateSicknessBegan" - {

        "must keep WhenDidYouLastWork" in {
          val answers = UserAnswers("id")
            .set(WhenDidYouLastWorkPage, LocalDate.of(2022, 2, 1)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(WhenDidYouLastWorkPage).value mustBe LocalDate.of(2022, 2, 1)
        }

      }

      "and WhenDidYouLastWork is on DateSicknessBegan" - {

        "must remove WhenDidYouLastWork" in {
          val answers = UserAnswers("id")
            .set(WhenDidYouLastWorkPage, LocalDate.of(2022, 2, 2)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 2)).success.value
          answers.get(WhenDidYouLastWorkPage) mustBe empty
        }

      }

      "and WhenDidYouLastWork is after DateSicknessBegan" - {

        "must remove WhenDidYouLastWork" in {
          val answers = UserAnswers("id")
            .set(WhenDidYouLastWorkPage, LocalDate.of(2022, 2, 2)).success.value
            .set(DateSicknessBeganPage, LocalDate.of(2022, 2, 1)).success.value
          answers.get(WhenDidYouLastWorkPage) mustBe empty
        }

      }

    }
  }
}
