package models

import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Mocker(
  status: Int,
  contentType: String,
  charset: String,
  location: Option[String],
  body: String,
  headerNames: List[String],
  headerValues: List[String])

object Mocker {

  lazy val version = current.configuration.getString("version").getOrElse("undefined")

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

  def toGist(mocker: Mocker): Gist = {
    import Gist._

    val headers = Map(
      "Content-Type" -> Some(s"${mocker.contentType}; charset=${mocker.charset.toLowerCase}"),
      "Location" -> mocker.location
    ).filter(_._2.isDefined).mapValues(_.get)

    val customheaders = mocker.headerNames.zip(mocker.headerValues).flatMap {
      case (name, value) if !name.trim.isEmpty && !value.trim.isEmpty => Some(name -> value)
      case _ => None
    }.toMap

    Gist(
      description = "Powered by Mocky",
      files = Map(
        "content" -> GistFile(Content.encode(mocker.body, "utf-8")),
        "metadata" -> GistFile(Json.stringify(Json.toJson(Metadata(
          mocker.status,
          mocker.charset,
          headers ++ customheaders,
          version
        ))))
      )
    )
  }
}
