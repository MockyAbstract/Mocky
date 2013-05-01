package models

import scala.concurrent.Future
import play.api.libs.ws.WS
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.concurrent.Execution.Implicits._
import org.slf4j.LoggerFactory

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
  contentUrl: String) {
  def fetchContent() = WS.url(contentUrl).get.flatMap(Gist.parseContent)
  def fetchMetadata() = WS.url(metadataUrl).get.flatMap(Gist.parseMetadata)
}

case class Metadata(
  status: Int,
  charset: String,
  headers: Map[String, String],
  version: String)

object Gist {

  implicit val logger = LoggerFactory.getLogger("ws")

  // ---- JSON ----
  implicit val gistFileWriter = Json.writes[GistFile]
  implicit val gistWriter = Json.writes[Gist]
  implicit val metadataFormat = Json.format[Metadata]
  implicit val gistResponseWriter = Json.writes[GistResponse]
  implicit val gistResponseReader = (
    (__ \ "url").read[String] and
    (__ \ "id").read[String] and
    (__ \ "description").read[String] and
    (__ \ "files" \ "metadata" \ "raw_url").read[String] and
    (__ \ "files" \ "content" \ "raw_url").read[String]
  )(GistResponse.apply _)

  // ---- WS ----
  def create(mock: Mocker): Future[GistResponse] = {
    val body = Json.toJson(Mocker.toGist(mock))
    val url = "https://api.github.com/gists"
    logger.debug(s">> POST $url with ${Json.stringify(body)}")
    WS.url(url).post(body).flatMap(parseGistResponse)
  }

  def get(gistId: String): Future[GistResponse] = {
    val url = s"https://api.github.com/gists/$gistId"
    logger.debug(s">> GET $url")
    WS.url(url).get.flatMap(parseGistResponse)
  }

  // ---- Parsers ----
  def parseGistResponse(response: play.api.libs.ws.Response): Future[GistResponse] = {
    if (response.status < 400) {
      logger.debug(s"<< (${response.status}) ${response.body}")
      response.json.validate[GistResponse].fold(
        error => {
          logger.error(s"Unable to parse GistResponse: $error")
          Future.failed(new RuntimeException("parse-json-failed"))
        },
        gistResponse => Future.successful(gistResponse))
    } else {
      logger.error("Unable to parse GitResponse, cannot contact WS\n" + debugResponse(response))
      Future.failed(new RuntimeException("ws-error"))
    }
  }

  def parseMetadata(response: play.api.libs.ws.Response): Future[Metadata] = {
    if (response.status < 400) {
      logger.debug(s"<< (${response.status}) ${response.body}")
      response.json.validate[Metadata].fold(
        error => {
          logger.error(s"Unable to parse GistMetadata: $error")
          Future.failed(new RuntimeException("parse-json-failed"))
        },
        metadata => Future.successful(metadata))
    } else {
      logger.error("Unable to parse GistMetadata, cannot contact WS\n" + debugResponse(response))
      Future.failed(new RuntimeException("ws-error"))
    }
  }

  def parseContent(response: play.api.libs.ws.Response): Future[String] = {
    if (response.status < 400)
      Future.successful(response.body)
    else {
      logger.error("Unable to parse GistContent, cannot contact WS\n" + debugResponse(response))
      Future.failed(new RuntimeException("ws-error"))
    }
  }

  private def debugResponse(r: play.api.libs.ws.Response) = {
    s"(${r.status}) ${r.body}})"
  }
}
