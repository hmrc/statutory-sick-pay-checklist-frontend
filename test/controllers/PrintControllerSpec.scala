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

package controllers

import audit.AuditService
import base.SpecBase
import com.dmanchester.playfop.sapi.PlayFop
import models.{JourneyModel, WhatIsYourName, WhatTimeDidYouFinish}
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchersSugar.eqTo
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.EitherValues
import org.scalatestplus.mockito.MockitoSugar
import pages._
import play.api.http.HeaderNames
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.domain.Nino
import views.html.PrintView

import java.time.LocalDate

class PrintControllerSpec extends SpecBase with EitherValues with MockitoSugar {

  val answers = emptyUserAnswers
    .set(WhatIsYourNamePage, WhatIsYourName("first", "last")).success.value
    .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
    .set(WhatIsYourNinoPage, Nino("AA123456A")).success.value
    .set(WhatIsYourDateOfBirthPage, LocalDate.of(2000, 2, 1)).success.value
    .set(PhoneNumberPage, "07875242851").success.value
    .set(DoYouKnowYourClockOrPayrollNumberPage, true).success.value
    .set(WhatIsYourClockOrPayrollNumberPage, "prcn").success.value
    .set(DetailsOfSicknessPage, "some details").success.value
    .set(DateSicknessBeganPage, LocalDate.of(2001, 2, 1)).success.value
    .set(HasSicknessEndedPage, true).success.value
    .set(DateSicknessEndedPage, LocalDate.of(2002, 2, 1)).success.value
    .set(CausedByAccidentOrIndustrialDiseasePage, true).success.value
    .set(WhenDidYouLastWorkPage, LocalDate.of(2003, 2, 1)).success.value
    .set(WhatTimeDidYouFinishPage, WhatTimeDidYouFinish(9, 0, "am")).success.value

  val model = JourneyModel.from(answers).right.value

  "next steps" - {

    "must return OK and the correct view" in {
      val application = applicationBuilder(userAnswers = Some(answers)).build()
      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onPageLoad().url)
        val result = route(application, request).value
        val view = application.injector.instanceOf[PrintView]
        status(result) mustEqual OK
        contentAsString(result) mustEqual view(model)(request, messages(application)).toString
      }
    }

    "must redirect to the journey recovery controller when the user answers are incomplete" in {
      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()
      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onPageLoad().url)
        val result = route(application, request).value
        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }

  "print form" - {

    "must return OK and the correct view" in {
      val mockAuditService = mock[AuditService]
      val mockPlayFop = mock[PlayFop]
      val application = applicationBuilder(userAnswers = Some(answers))
        .overrides(
          bind[AuditService].toInstance(mockAuditService),
          bind[PlayFop].toInstance(mockPlayFop)
        )
        .build()
      when(mockPlayFop.processTwirlXml(any(), any(), any(), any())).thenReturn("hello".getBytes)
      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onDownload().url)
        val result = route(application, request).value
        verify(mockAuditService, times(1)).auditDownload(eqTo(model))(any())
        status(result) mustEqual OK
        contentAsString(result) mustEqual "hello"
        header(HeaderNames.CONTENT_DISPOSITION, result).value mustEqual "attachment; filename=claim-statutory-sick-pay-sc2.pdf"
      }
    }

    "must redirect to the journey recovery controller when the user answers are incomplete" in {
      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()
      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onDownload().url)
        val result = route(application, request).value
        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
