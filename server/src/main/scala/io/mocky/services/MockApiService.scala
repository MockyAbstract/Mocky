package io.mocky.services

import cats.effect.IO
import io.circe.Decoder

import io.mocky.http.JsonMarshalling
import io.mocky.models.errors.MockNotFoundError
import io.mocky.models.mocks.actions.{ CreateUpdateMock, DeleteMock }
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.middleware.CORS

import io.mocky.config.Settings
import io.mocky.models.mocks.MockCreatedResponse
import io.mocky.repositories.MockV3Repository
import io.mocky.utils.HttpUtil

class MockApiService(repository: MockV3Repository, settings: Settings) extends Http4sDsl[IO] with JsonMarshalling {

  // Allow only request coming from `settings.cors.domain` and `settings.cors.devDomain` if env == dev
  private val corsAPIConfig = {
    val allowedDevDomains = if (settings.environment == "dev") settings.cors.devDomains else None
    val allowedDomains = Seq(settings.cors.domain) ++ allowedDevDomains.getOrElse(Nil)
    CORS.DefaultCORSConfig.copy(
      anyOrigin = false,
      allowedOrigins = origin => allowedDomains.contains(origin)
    )
  }

  // Expose the routes wrapped into their middleware
  lazy val routing: HttpRoutes[IO] = CORS(routes, corsAPIConfig)

  // Prepare a decoder with dynamic configuration
  implicit private val createUpdateMockDecoder: Decoder[CreateUpdateMock] = CreateUpdateMock.decoder(settings.mock)

  private def routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    // Create new mock
    case req @ POST -> Root / "api" / "mock" =>
      decodeJson[IO, CreateUpdateMock](req) { createMock =>
        for {
          created <- repository.insert(createMock.withIp(HttpUtil.getIP(req)))
          mock = MockCreatedResponse(created, createMock, settings.endpoint)
          response <- Created(mock)
        } yield response
      }

    // Get an existing mock
    case GET -> Root / "api" / "mock" / UUIDVar(id) =>
      repository.get(id).flatMap {
        case Left(MockNotFoundError) => NotFound()
        case Right(mock) => Ok(mock)
      }

    // Get the stats of a mock
    case GET -> Root / "api" / "mock" / UUIDVar(id) / "stats" =>
      repository.stats(id).flatMap {
        case Left(MockNotFoundError) => NotFound()
        case Right(stats) => Ok(stats)
      }

    // Update an existing mock
    case req @ PUT -> Root / "api" / "mock" / UUIDVar(id) =>
      decodeJson[IO, CreateUpdateMock](req) { updateMock =>
        for {
          updated <- repository.update(id, updateMock.withIp(HttpUtil.getIP(req)))
          response <- if (updated) NoContent() else NotFound()
        } yield response
      }

    // Delete an existing mock
    case req @ DELETE -> Root / "api" / "mock" / UUIDVar(id) =>
      decodeJson[IO, DeleteMock](req) { deleteMock =>
        for {
          deleted <- repository.delete(id, deleteMock)
          response <- if (deleted) NoContent() else NotFound()
        } yield response
      }

    // Check if a mock can be deleted with this secret
    case req @ POST -> Root / "api" / "mock" / UUIDVar(id) / "check" =>
      decodeJson[IO, DeleteMock](req) { deleteMock =>
        for {
          result <- repository.checkDeletionSecret(id, deleteMock)
          response <- Ok(result)
        } yield response
      }

  }

}
