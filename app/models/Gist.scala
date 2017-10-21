package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Gist(
  description: String,
  files: Map[String, GistFile],
  public: Boolean = false)

case class GistFile(
  content: String)

case class GistResponse(
  url: String,
  id: String,
  description: String,
  metadataUrl: String,
  contentUrl: String)

object Gist {

  implicit val gistFileFormat: OFormat[GistFile] = Json.format[GistFile]
  implicit val gistFormat: OFormat[Gist] = Json.format[Gist]
  implicit val gistResponseWriter: OWrites[GistResponse] = Json.writes[GistResponse]
  implicit val gistResponseReader: Reads[GistResponse] = (
    (__ \ "url").read[String] and
    (__ \ "id").read[String] and
    (__ \ "description").read[String] and
    (__ \ "files" \ "metadata" \ "raw_url").read[String] and
    (__ \ "files" \ "content" \ "raw_url").read[String]
  )(GistResponse.apply _)

}
