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

package generators

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.domain.Nino

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryDoYouKnowYourNationalInsuranceNumberUserAnswersEntry: Arbitrary[(DoYouKnowYourNationalInsuranceNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DoYouKnowYourNationalInsuranceNumberPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPhoneNumberUserAnswersEntry: Arbitrary[(PhoneNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PhoneNumberPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCausedByAccidentOrIndustrialDiseaseUserAnswersEntry: Arbitrary[(CausedByAccidentOrIndustrialDiseasePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CausedByAccidentOrIndustrialDiseasePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatTimeDidYouFinishUserAnswersEntry: Arbitrary[(WhatTimeDidYouFinishPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatTimeDidYouFinishPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhenDidYouLastWorkUserAnswersEntry: Arbitrary[(WhenDidYouLastWorkPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhenDidYouLastWorkPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDateSicknessEndedUserAnswersEntry: Arbitrary[(DateSicknessEndedPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DateSicknessEndedPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryHasSicknessEndedUserAnswersEntry: Arbitrary[(HasSicknessEndedPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[HasSicknessEndedPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDateSicknessBeganUserAnswersEntry: Arbitrary[(DateSicknessBeganPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DateSicknessBeganPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDetailsOfSicknessUserAnswersEntry: Arbitrary[(DetailsOfSicknessPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DetailsOfSicknessPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourClockOrPayrollNumberUserAnswersEntry: Arbitrary[(WhatIsYourClockOrPayrollNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourClockOrPayrollNumberPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDoYouKnowYourClockOrPayrollNumberUserAnswersEntry: Arbitrary[(DoYouKnowYourClockOrPayrollNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DoYouKnowYourClockOrPayrollNumberPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourDateOfBirthUserAnswersEntry: Arbitrary[(WhatIsYourDateOfBirthPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourDateOfBirthPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourNinoUserAnswersEntry: Arbitrary[(WhatIsYourNinoPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourNinoPage.type]
        value <- arbitrary[Nino].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourNameUserAnswersEntry: Arbitrary[(WhatIsYourNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourNamePage.type]
        value <- arbitrary[WhatIsYourName].map(Json.toJson(_))
      } yield (page, value)
    }
}
