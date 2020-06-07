package io.mocky.models.mocks

import io.circe.Json
import org.http4s._
import org.http4s.headers.`Content-Type`

final case class MockResponse(
  content: Option[Array[Byte]],
  charset: Charset,
  status: Status,
  contentType: `Content-Type`,
  headers: Headers
)

object MockResponse {
  def apply(mock: Mock): MockResponse = {
    val status = parseStatus(mock.status)
    val charset = parseCharset(mock.charset)
    val headers = mock.headers.map(parseHeaders).getOrElse(Headers.empty)
    val contentType = `Content-Type`.parse(mock.contentType)
      .getOrElse(DEFAULT_CONTENT_TYPE)
      .withCharset(charset)

    MockResponse(mock.content, charset, status, contentType, headers)
  }

  val DEFAULT_CHARSET = Charset.`UTF-8`
  val DEFAULT_CONTENT_TYPE = `Content-Type`(MediaType.application.json)

  private def parseCharset(charset: String) = Charset.fromString(charset).getOrElse(DEFAULT_CHARSET)
  private def parseStatus(status: Int) = Status.fromInt(status).getOrElse(Status.Ok)
  private def parseHeaders(headers: Json) = Headers(
    headers.as[Map[String, String]].getOrElse(Map.empty).view.map((Header.apply _).tupled).to(List))
}
