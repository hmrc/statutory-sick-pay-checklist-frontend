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

package generators

import models._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import uk.gov.hmrc.domain.Nino

trait ModelGenerators {

  implicit lazy val arbitraryWhatIsYourName: Arbitrary[WhatIsYourName] =
    Arbitrary {
      for {
        firstname <- arbitrary[String]
        surname <- arbitrary[String]
      } yield WhatIsYourName(firstname, surname)
    }

  implicit lazy val arbitraryNino: Arbitrary[Nino] = Arbitrary {
    for {
      firstChar <- Gen.oneOf('A', 'C', 'E', 'H', 'J', 'L', 'M', 'O', 'P', 'R', 'S', 'W', 'X', 'Y').map(_.toString)
      secondChar <- Gen.oneOf('A', 'B', 'C', 'E', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'W', 'X', 'Y', 'Z').map(_.toString)
      digits <- Gen.listOfN(6, Gen.numChar)
      lastChar <- Gen.oneOf('A', 'B', 'C', 'D')
    } yield Nino(firstChar ++ secondChar ++ digits :+ lastChar)
  }

  implicit lazy val arbitraryTime: Arbitrary[WhatTimeDidYouFinish] = Arbitrary {
    for {
      hour <- Gen.choose(1, 12)
      minute <- Gen.choose(0, 59)
      ampm <- Gen.oneOf(List("am", "pm"))
    } yield WhatTimeDidYouFinish(hour, minute, ampm)
  }
}
