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

package forms.mappings

import models.WhatTimeDidYouFinish
import play.api.data.FormError
import play.api.data.format.Formatter

private[mappings] class TimeFinishedFormatter(
                                               invalidKey: String,
                                               requiredKey: String,
                                               twoRequiredKey: String,
                                               amPmRequiredKey: String,
                                               invalidOverall: String,
                                               args: Seq[String] = Seq.empty
                                          ) extends Formatter[WhatTimeDidYouFinish] with Formatters {

  private val mandatoryFieldKeys: List[String] = List("hour", "minute")

  private def toTimeAm(key: String, hour: Int, minute: Int): Either[Seq[FormError], WhatTimeDidYouFinish] = {
    if (hour == 0) {
      Right(WhatTimeDidYouFinish(12, minute, "am"))
    } else if (hour <= 12) {
      Right(WhatTimeDidYouFinish(hour, minute, "am"))
    } else {
      Left(Seq(FormError(key, invalidOverall, args)))
    }
  }

  private def toTimePm(key: String, hour: Int, minute: Int): Either[Seq[FormError], WhatTimeDidYouFinish] = {
    if (hour == 0) {
      Left(Seq(FormError(key, invalidOverall, args)))
    } else if (hour > 12) {
      Right(WhatTimeDidYouFinish(hour - 12, minute, "pm"))
    } else {
      Right(WhatTimeDidYouFinish(hour, minute, "pm"))
    }
  }

  private def toTimeNoAmPm(key: String, hour: Int, minute: Int): Either[Seq[FormError], WhatTimeDidYouFinish] = {
    if (hour == 0) {
      Right(WhatTimeDidYouFinish(12, minute, "am"))
    } else if (hour > 12) {
      Right(WhatTimeDidYouFinish(hour - 12, minute, "pm"))
    } else {
      Left(Seq(FormError(key, amPmRequiredKey, args :+ "ampm")))
    }
  }

  private def toTime(key: String, hour: Int, minute: Int, amPm: Option[String]): Either[Seq[FormError], WhatTimeDidYouFinish] = {
    amPm match {
      case Some("am") => toTimeAm(key, hour, minute)
      case Some("pm") => toTimePm(key, hour, minute)
      case _ => toTimeNoAmPm(key, hour, minute)
    }
  }

  private def formatTime(key: String, data: Map[String, String]): Either[Seq[FormError], WhatTimeDidYouFinish] = {

    val int = intFormatter(
      requiredKey = invalidKey,
      wholeNumberKey = invalidKey,
      nonNumericKey = invalidKey,
      args
    )

    def intInRange(value: Int, key: String, min: Int, max: Int): Either[Seq[FormError], Int] = {
      if (value < min || value > max) {
        Left(Seq(FormError(key, invalidKey, args)))
      } else {
        Right(value)
      }
    }

    def amOrPm(value: Option[String], key: String): Either[Seq[FormError], Option[String]] = {
      value match {
        case Some(value) if List("am", "pm").contains(value) => Right(Some(value))
        case Some("") => Right(None)
        case _ => Left(Seq(FormError(key, amPmRequiredKey, args :+ "ampm")))
      }
    }

    val hour   = ("hour",   int.bind(s"$key.hour", data).flatMap(intInRange(_, key, 0, 23)))
    val minute = ("minute", int.bind(s"$key.minute", data).flatMap(intInRange(_, key, 0, 59)))
    val amPm = ("am or pm", amOrPm(data.get(s"$key.ampm"), key))

    val invalidFields =
      Seq(hour, minute)
        .filter(_._2.isLeft)
        .map(_._1)

    if (invalidFields.nonEmpty) {
      Left(Seq(FormError(key, invalidKey, invalidFields ++ args)))
    } else {
      for {
        hour <- hour._2.right
        minute <- minute._2.right
        amPm <- amPm._2.right
        time <- toTime(key, hour, minute, amPm)
      } yield time
    }
  }

  override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], WhatTimeDidYouFinish] = {
    val mandatoryFields = mandatoryFieldKeys.map {
      field =>
        field -> data.get(s"$key.$field").filter(_.nonEmpty)
    }.toMap

    lazy val missingFields = mandatoryFields
      .withFilter(_._2.isEmpty)
      .map(_._1)
      .toList

    missingFields.size match {
      case 2 => Left(List(FormError(key, twoRequiredKey, missingFields ++ args)))
      case 1 => Left(List(FormError(key, requiredKey, missingFields ++ args)))
      case 0 => formatTime(key, data)
    }
  }

  override def unbind(key: String, value: WhatTimeDidYouFinish): Map[String, String] =
    Map(
      s"$key.hour" -> value.hour.toString,
      s"$key.minute" -> value.minute.toString,
      s"$key.ampm" -> value.amOrPm
    )
}
