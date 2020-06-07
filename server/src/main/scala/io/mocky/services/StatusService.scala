package io.mocky.services

import buildinfo.BuildInfo
import cats.effect.IO
import io.circe.Json
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class StatusService() extends Http4sDsl[IO] {

  val routes = HttpRoutes.of[IO] {
    case GET -> Root / "api" / "status" =>
      Ok(
        Json.obj(
          "name" -> BuildInfo.name.asJson,
          "version" -> BuildInfo.version.asJson,
          "build_at" -> BuildInfo.builtAtString.asJson
        ))
  }
}
