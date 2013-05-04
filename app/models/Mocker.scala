package models

import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.MockResponse._

/**
 * Map client Form data
 */
case class Mocker(
  status: Int,
  contentType: String,
  charset: String,
  location: Option[String],
  body: String,
  headerNames: List[String],
  headerValues: List[String]) {

  def headers = {
    val headers = Map(
      "Content-Type" -> Some(s"${contentType}; charset=${charset.toLowerCase}"),
      "Location" -> location
    ).filter(_._2.isDefined).mapValues(_.get)

    val customheaders = headerNames.zip(headerValues).flatMap {
      case (name, value) if !name.trim.isEmpty && !value.trim.isEmpty => Some(name -> value)
      case _ => None
    }.toMap

    headers ++ customheaders
  }

}

object Mocker {

  val formMocker = Form(
    mapping(
      "statuscode"  -> number.verifying(min(0), max(9999)),
      "contenttype" -> nonEmptyText,
      "charset"     -> nonEmptyText,
      "location"    -> optional(text),
      "body"        -> text,
      "headerNames"  -> list(text),
      "headerValues"  -> list(text)
    )(Mocker.apply)(Mocker.unapply)
  )

}
