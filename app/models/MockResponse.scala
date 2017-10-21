package models

import play.api.http.HeaderNames
import play.api.libs.json._
import play.api.mvc.Headers

case class Metadata(
  status: Int,
  charset: String,
  private val headers: Map[String, String],
  version: String) {

  private val httpHeaders = Headers(headers.toSeq: _*)

  val headersWithoutContentType = httpHeaders.remove(HeaderNames.CONTENT_TYPE).headers
  val contentType = httpHeaders.get(HeaderNames.CONTENT_TYPE).getOrElse("application/json")
}

case class MockResponse(
  content: String,
  metadata: Metadata)

object MockResponse {
  implicit val metadataFormat: OFormat[Metadata] = Json.format[Metadata]
  implicit val mockResponseFormat: OFormat[MockResponse] = Json.format[MockResponse]
}
