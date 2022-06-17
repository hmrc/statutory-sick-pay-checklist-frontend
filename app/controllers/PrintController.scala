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

import audit.AuditService
import com.dmanchester.playfop.sapi.PlayFop
import controllers.actions._
import models.JourneyModel
import org.apache.fop.apps.FOUserAgent
import org.apache.xmlgraphics.util.MimeConstants

import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.PrintView
import views.xml.pdf.PdfView

class PrintController @Inject()(
                                 override val messagesApi: MessagesApi,
                                 identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 val controllerComponents: MessagesControllerComponents,
                                 view: PrintView,
                                 pdfView: PdfView,
                                 auditService: AuditService,
                                 fop: PlayFop
                               ) extends FrontendBaseController with I18nSupport {

  private val userAgentBlock: FOUserAgent => Unit = { foUserAgent: FOUserAgent =>
    foUserAgent.setAccessibility(true)
    foUserAgent.setPdfUAEnabled(true)
    foUserAgent.setAuthor("HMRC forms service")
    foUserAgent.setProducer("HMRC forms services")
    foUserAgent.setCreator("HMRC forms services")
    foUserAgent.setSubject("Ask your employer for Statutory Sick Pay")
    foUserAgent.setTitle("Ask your employer for Statutory Sick Pay")
  }

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      JourneyModel.from(request.userAnswers).map { model =>
        Ok(view(model))
      }.getOrElse(Redirect(routes.JourneyRecoveryController.onPageLoad()))
  }

  def onDownload: Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      JourneyModel.from(request.userAnswers).map { model =>
        auditService.auditDownload(model)
        val pdf = fop.processTwirlXml(pdfView(model), MimeConstants.MIME_PDF, foUserAgentBlock = userAgentBlock)
        Ok(pdf)
          .as("application/octet-stream")
          .withHeaders(CONTENT_DISPOSITION -> "attachment; filename=claim-statutory-sick-pay-sc2.pdf")
      }.getOrElse(Redirect(routes.JourneyRecoveryController.onPageLoad()))
  }
}
