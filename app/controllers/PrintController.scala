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
import controllers.actions._
import models.JourneyModel
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.FopService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.PrintView
import views.xml.pdf.PdfView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PrintController @Inject()(
                                 override val messagesApi: MessagesApi,
                                 identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 val controllerComponents: MessagesControllerComponents,
                                 view: PrintView,
                                 pdfView: PdfView,
                                 fopService: FopService,
                                 auditService: AuditService,
                               )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {


  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      JourneyModel.from(request.userAnswers).map { model =>
        Ok(view(model))
      }.getOrElse(Redirect(routes.JourneyRecoveryController.onPageLoad()))
  }

  def onDownload: Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      JourneyModel.from(request.userAnswers).map { model =>
        auditService.auditDownload(model)
        fopService.render(pdfView.render(model, implicitly, implicitly).body).map { pdf =>
          Ok(pdf)
            .as("application/octet-stream")
            .withHeaders(CONTENT_DISPOSITION -> "attachment; filename=claim-statutory-sick-pay-sc2.pdf")
        }
      }.getOrElse(Future.successful(Redirect(routes.JourneyRecoveryController.onPageLoad())))
  }
}
