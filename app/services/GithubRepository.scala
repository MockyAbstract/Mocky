package services

import scala.concurrent.Future
import org.slf4j.LoggerFactory

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WS
import play.api.libs.json.Json

import models._
import models.Gist._
import models.MockResponse._
import com.ning.http.util.Base64

object GithubRepository extends IRepository {

  private val logger = LoggerFactory.getLogger("ws.github")

  def getMockFromId(id: String): Future[MockResponse] = {
    for {
      gist <- WS.url(s"https://api.github.com/gists/$id").get.flatMap(parseGistResponse)
      content <- WS.url(gist.contentUrl).get.flatMap(parseContent)
      metadata <- WS.url(gist.metadataUrl).get.flatMap(parseMetadata)
    } yield MockResponse(content, metadata)
  }

  def save(mock: Mocker): Future[String] = {
    val body = Json.toJson(toGist(mock))
    val url = "https://api.github.com/gists"
    WS.url(url).post(body).flatMap(parseGistResponse).map(_.id)
  }

  private def toGist(mocker: Mocker): Gist = {
    Gist(
      description = "Powered by Mocky",
      files = Map(
        "content" -> GistFile(encodeBody(mocker.body)),
        "metadata" -> GistFile(Json.stringify(Json.toJson(Metadata(
          mocker.status,
          mocker.charset,
          mocker.headers,
          Repository.version
        ))))
      )
    )
  }

  private def parseGistResponse(response: play.api.libs.ws.Response): Future[GistResponse] = {
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

  private def parseMetadata(response: play.api.libs.ws.Response): Future[Metadata] = {
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

  private def parseContent(response: play.api.libs.ws.Response): Future[String] = {
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

  def encodeBody(content: String, charset: String): String = Base64.encode(("|"+content).getBytes(charset))

  def decodeBody(content: String, charset: String): String = new String(Base64.decode(content).drop(1), charset)
}
