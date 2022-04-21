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

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

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
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
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
