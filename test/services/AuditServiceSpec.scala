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

package services

import audit.{AuditService, DownloadAuditEvent}
import models.{JourneyModel, WhatTimeDidYouFinish}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.bind
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

import java.time.LocalDate

class AuditServiceSpec extends AnyFreeSpec with Matchers with MockitoSugar {

  private val mockAuditConnector = mock[AuditConnector]

  private val application = new GuiceApplicationBuilder()
    .configure(
      "auditing.downloadEventName" -> "eventName"
    )
    .overrides(
      bind[AuditConnector].toInstance(mockAuditConnector)
    ).build()

  private val service = application.injector.instanceOf[AuditService]

  "auditDownload" - {

    "must audit the correct information" in {

      val model = JourneyModel(
        firstName = "first",
        surname = "surname",
        nino = Some("AA123456A"),
        dob = LocalDate.of(1990, 2, 1),
        telephoneNumber = "tel",
        payrollOrClockNumber = Some("prcn"),
        sicknessDetails = "some details",
        sicknessStartDate = LocalDate.of(2000, 3, 2),
        sicknessEndDate = Some(LocalDate.of(2000, 4, 3)),
        whenDidYouLastWork = LocalDate.of(2000, 2, 1),
        whatTimeDidYouFinish = WhatTimeDidYouFinish(12, 0, "pm"),
        causedByIndustrialDiseaseOrAccident = true
      )

      val expected = DownloadAuditEvent(
        firstName = "first",
        surname = "surname",
        nino = Some("AA123456A"),
        dob = LocalDate.of(1990, 2, 1),
        telephoneNumber = "tel",
        payrollOrClockNumber = Some("prcn"),
        sicknessDetails = "some details",
        sicknessStartDate = LocalDate.of(2000, 3, 2),
        sicknessEndDate = Some(LocalDate.of(2000, 4, 3)),
        whenDidYouLastWork = LocalDate.of(2000, 2, 1),
        whatTimeDidYouFinish = "12:00pm",
        causedByIndustrialDiseaseOrAccident = true
      )

      val hc = HeaderCarrier()
      service.auditDownload(model)(hc)

      verify(mockAuditConnector, times(1)).sendExplicitAudit(eqTo("eventName"), eqTo(expected))(eqTo(hc), any(), any())
    }
  }
}
