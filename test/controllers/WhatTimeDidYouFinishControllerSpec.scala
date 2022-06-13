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

package controllers

import base.SpecBase
import forms.WhatTimeDidYouFinishFormProvider
import models.{NormalMode, UserAnswers, WhatTimeDidYouFinish}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.{WhatTimeDidYouFinishPage, WhenDidYouLastWorkPage}
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.WhatTimeDidYouFinishView

import java.time.LocalDate
import scala.concurrent.Future

class WhatTimeDidYouFinishControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new WhatTimeDidYouFinishFormProvider()
  val form = formProvider()
  val lastWorkedDate = LocalDate.of(2010, 1, 1)
  val userAnswersWithDate = emptyUserAnswers.set(WhenDidYouLastWorkPage, lastWorkedDate).success.value

  lazy val whatTimeDidYouFinishRoute = routes.WhatTimeDidYouFinishController.onPageLoad(NormalMode).url

  "WhatTimeDidYouFinish Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(userAnswersWithDate)).build()

      running(application) {
        val request = FakeRequest(GET, whatTimeDidYouFinishRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[WhatTimeDidYouFinishView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, lastWorkedDate, NormalMode)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = userAnswersWithDate.set(WhatTimeDidYouFinishPage, WhatTimeDidYouFinish(10, 0, "am")).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, whatTimeDidYouFinishRoute)

        val view = application.injector.instanceOf[WhatTimeDidYouFinishView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(WhatTimeDidYouFinish(10, 0, "am")), lastWorkedDate, NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(userAnswersWithDate))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[SessionRepository].toInstance(mockSessionRepository)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, whatTimeDidYouFinishRoute)
            .withFormUrlEncodedBody(
              "time-finished-hour" -> "10",
              "time-finished-minute" -> "0",
              "time-finished-ampm" -> "am"
            )

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(userAnswersWithDate)).build()

      running(application) {
        val request =
          FakeRequest(POST, whatTimeDidYouFinishRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[WhatTimeDidYouFinishView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, lastWorkedDate, NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if WhenDidYouLastWorkPage hasn't been answered" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, whatTimeDidYouFinishRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, whatTimeDidYouFinishRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if WhenDidYouLastWorkPage hasn't been answered" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, whatTimeDidYouFinishRoute)
            .withFormUrlEncodedBody(("value", "10:00am"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, whatTimeDidYouFinishRoute)
            .withFormUrlEncodedBody(("value", "10:00am"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
