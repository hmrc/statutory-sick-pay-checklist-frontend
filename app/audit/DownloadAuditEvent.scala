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

package audit

import models.JourneyModel
import play.api.libs.json.{Format, Json}

import java.time.LocalDate

final case class DownloadAuditEvent(
                                     firstName: String,
                                     surname: String,
                                     nino: Option[String],
                                     dob: LocalDate,
                                     telephoneNumber: String,
                                     payrollOrClockNumber: Option[String],
                                     sicknessDetails: String,
                                     sicknessStartDate: LocalDate,
                                     sicknessEndDate: Option[LocalDate],
                                     whenDidYouLastWork: LocalDate,
                                     whatTimeDidYouFinish: String,
                                     causedByIndustrialDiseaseOrAccident: Boolean
                                   )

object DownloadAuditEvent {

  def from(model: JourneyModel): DownloadAuditEvent =
    DownloadAuditEvent(
      firstName = model.firstName,
      surname = model.surname,
      nino = model.nino,
      dob = model.dob,
      telephoneNumber = model.telephoneNumber,
      payrollOrClockNumber = model.payrollOrClockNumber,
      sicknessDetails = model.sicknessDetails,
      sicknessStartDate = model.sicknessStartDate,
      sicknessEndDate = model.sicknessEndDate,
      whenDidYouLastWork = model.whenDidYouLastWork,
      whatTimeDidYouFinish = model.whatTimeDidYouFinish,
      causedByIndustrialDiseaseOrAccident = model.causedByIndustrialDiseaseOrAccident
    )

  implicit lazy val format: Format[DownloadAuditEvent] = Json.format
}
