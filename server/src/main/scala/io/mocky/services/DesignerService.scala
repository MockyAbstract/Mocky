package io.mocky.services

import cats.effect.IO
import org.http4s.{ Headers, HttpRoutes, Response, Status, Uri }
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Location

import io.mocky.config.CorsSettings

class DesignerService(corsSettings: CorsSettings) extends Http4sDsl[IO] {

  val routes = HttpRoutes.of[IO] {
    case GET -> Root =>
      IO.pure(
        Response[IO](
          Status.MovedPermanently,
          headers = Headers.of(
            Location(Uri.unsafeFromString(corsSettings.domain))
          )
        ))
  }

}
