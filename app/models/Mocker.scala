package models

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

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

  def headers: Map[String, String] = {
    val requiredHeaders = Map(
      "Content-Type" -> Some(s"$contentType; charset=${charset.toLowerCase}"),
      "Location" -> location
    ).filter(_._2.isDefined).mapValues(_.get)

    val customHeaders = headerNames.zip(headerValues).flatMap {
      case (name, value) if !name.trim.isEmpty && !value.trim.isEmpty => Some(name -> value)
      case _ => None
    }(collection.breakOut)

    requiredHeaders ++ customHeaders
  }

}

object Mocker {

  val formMocker = Form(
    mapping(
      "statuscode"    -> number.verifying(min(0), max(9999)),
      "contenttype"   -> nonEmptyText,
      "charset"       -> nonEmptyText,
      "location"      -> optional(text),
      "body"          -> text,
      "headerNames"   -> list(text),
      "headerValues"  -> list(text)
    )(Mocker.apply)(Mocker.unapply)
  )

}
