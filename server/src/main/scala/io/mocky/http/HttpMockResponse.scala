package io.mocky.http

import cats.Applicative
import cats.effect.IO
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Length`

import io.mocky.models.mocks.MockResponse

trait HttpMockResponse extends Http4sDsl[IO] {

  protected def respondWithMock[F[_]](mock: MockResponse)(implicit F: Applicative[IO]): IO[Response[IO]] = {

    val entity = prepareEntity(mock)
    val headers = prepareHeaders(mock, entity)

    F.pure(
      Response[IO](
        status = mock.status,
        headers = headers,
        body = entity.body
      )
    )
  }

  private def entityLengthHeader(entity: Entity[IO]): Headers = {
    entity.length
      .flatMap(length => `Content-Length`.fromLong(length).toOption)
      .map(header => Headers.of(header))
      .getOrElse(Headers.empty)
  }

  private def prepareEntity(mock: MockResponse): Entity[IO] = {
    mock.content match {
      case Some(content) => EntityEncoder.byteArrayEncoder[IO].toEntity(content)
      case None => Entity.empty
    }

  }

  private def prepareHeaders(mock: MockResponse, entity: Entity[IO]): Headers = {
    val userHeaders = mock.headers
    val lengthHeaders = entityLengthHeader(entity)
    val contentTypeHeader = Headers.of(mock.contentType)

    userHeaders ++ lengthHeaders ++ contentTypeHeader
  }

}
