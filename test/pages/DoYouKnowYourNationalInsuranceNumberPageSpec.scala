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
import pages.behaviours.PageBehaviours
import uk.gov.hmrc.domain.Nino

class DoYouKnowYourNationalInsuranceNumberPageSpec extends PageBehaviours {

  "DoYouKnowYourNationalInsuranceNumberPage" - {

    beRetrievable[Boolean](DoYouKnowYourNationalInsuranceNumberPage)

    beSettable[Boolean](DoYouKnowYourNationalInsuranceNumberPage)

    beRemovable[Boolean](DoYouKnowYourNationalInsuranceNumberPage)

    "must cleanup national insurance number when no" in {
      val userAnswers = UserAnswers("id")
        .set(WhatIsYourNinoPage, Nino("AA123456A")).success.value

      val result = userAnswers.set(DoYouKnowYourNationalInsuranceNumberPage, false).success.value

      result.get(WhatIsYourNinoPage) must not be defined
    }

    "must not cleanup national insurance number when yes" in {
      val userAnswers = UserAnswers("id")
        .set(WhatIsYourNinoPage, Nino("AA123456A")).success.value

      val result = userAnswers.set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value

      result.get(WhatIsYourNinoPage).value mustBe Nino("AA123456A")
    }
  }
}
