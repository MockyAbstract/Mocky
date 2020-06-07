package io.mocky.services

import cats.effect.IO
import io.circe.Json
import org.http4s._
import org.http4s.dsl.Http4sDsl
import io.circe.syntax._

import io.mocky.config.Settings
import io.mocky.http.JsonMarshalling
import io.mocky.http.middleware._
import io.mocky.models.Gate
import io.mocky.repositories.{ MockV2Repository, MockV3Repository }

class MockAdminApiService(repoV2: MockV2Repository, repoV3: MockV3Repository, settings: Settings)
    extends Http4sDsl[IO]
    with JsonMarshalling {

  // Expose the routes wrapped into their middleware
  lazy val routing: HttpRoutes[IO] = new Authorization(settings.admin).Administrator(routes)

  private def routes: AuthedRoutes[Role, IO] = AuthedRoutes.of[Role, IO] {

    // Delete an existing mock
    case DELETE -> Root / "api" / UUIDVar(id) as (user: Admin.type) =>
      implicit val adminGate = Gate(user)
      for {
        deleted <- repoV3.adminDelete(id)
        response <- if (deleted) NoContent() else NotFound()
      } yield response

    // Fetch global statistics
    case GET -> Root / "api" / "stats" as (user: Admin.type) =>
      implicit val adminGate = Gate(user)
      for {
        statsV2 <- repoV2.adminStats()
        statsV3 <- repoV3.adminStats()
        response <- Ok(
          Json.obj(
            "v2" -> statsV2.asJson,
            "v3" -> statsV3.asJson
          ))
      } yield response
  }

}
