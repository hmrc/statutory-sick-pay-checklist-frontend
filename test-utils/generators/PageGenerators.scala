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

import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryDoYouKnowYourNationalInsuranceNumberPage: Arbitrary[DoYouKnowYourNationalInsuranceNumberPage.type] =
    Arbitrary(DoYouKnowYourNationalInsuranceNumberPage)

  implicit lazy val arbitraryPhoneNumberPage: Arbitrary[PhoneNumberPage.type] =
    Arbitrary(PhoneNumberPage)

  implicit lazy val arbitraryCausedByAccidentOrIndustrialDiseasePage: Arbitrary[CausedByAccidentOrIndustrialDiseasePage.type] =
    Arbitrary(CausedByAccidentOrIndustrialDiseasePage)

  implicit lazy val arbitraryWhatTimeDidYouFinishPage: Arbitrary[WhatTimeDidYouFinishPage.type] =
    Arbitrary(WhatTimeDidYouFinishPage)

  implicit lazy val arbitraryWhenDidYouLastWorkPage: Arbitrary[WhenDidYouLastWorkPage.type] =
    Arbitrary(WhenDidYouLastWorkPage)

  implicit lazy val arbitraryDateSicknessEndedPage: Arbitrary[DateSicknessEndedPage.type] =
    Arbitrary(DateSicknessEndedPage)

  implicit lazy val arbitraryHasSicknessEndedPage: Arbitrary[HasSicknessEndedPage.type] =
    Arbitrary(HasSicknessEndedPage)

  implicit lazy val arbitraryDateSicknessBeganPage: Arbitrary[DateSicknessBeganPage.type] =
    Arbitrary(DateSicknessBeganPage)

  implicit lazy val arbitraryDetailsOfSicknessPage: Arbitrary[DetailsOfSicknessPage.type] =
    Arbitrary(DetailsOfSicknessPage)

  implicit lazy val arbitraryWhatIsYourClockOrPayrollNumberPage: Arbitrary[WhatIsYourClockOrPayrollNumberPage.type] =
    Arbitrary(WhatIsYourClockOrPayrollNumberPage)

  implicit lazy val arbitraryDoYouKnowYourClockOrPayrollNumberPage: Arbitrary[DoYouKnowYourClockOrPayrollNumberPage.type] =
    Arbitrary(DoYouKnowYourClockOrPayrollNumberPage)

  implicit lazy val arbitraryWhatIsYourDateOfBirthPage: Arbitrary[WhatIsYourDateOfBirthPage.type] =
    Arbitrary(WhatIsYourDateOfBirthPage)

  implicit lazy val arbitraryWhatIsYourNinoPage: Arbitrary[WhatIsYourNinoPage.type] =
    Arbitrary(WhatIsYourNinoPage)

  implicit lazy val arbitraryWhatIsYourNamePage: Arbitrary[WhatIsYourNamePage.type] =
    Arbitrary(WhatIsYourNamePage)
}
