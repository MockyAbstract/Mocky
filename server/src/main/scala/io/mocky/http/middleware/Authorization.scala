package io.mocky.http.middleware

import cats.data.{ Kleisli, OptionT }
import cats.effect.IO
import org.http4s.implicits._
import org.http4s.server.AuthMiddleware
import org.http4s.{ AuthedRoutes, Request, Response, Status }

import io.mocky.config.AdminSettings

sealed trait Role
case object Admin extends Role

sealed trait AuthenticationError {
  val message: String
  val status: Status
}
case class Unauthorized(message: String) extends AuthenticationError {
  val status = Status.Unauthorized
}
case class Forbidden(message: String) extends AuthenticationError {
  val status = Status.Forbidden
}

class Authorization(settings: AdminSettings) {

  /**
    * Basic Admin authorization, working by sending a token in `settings.admin.header` header that
    * will be matched with `settings.admin.token`
    */
  val Administrator = AuthMiddleware(authAdministrator, onFailure)

  private def onFailure: AuthedRoutes[AuthenticationError, IO] = {
    Kleisli(req => OptionT.liftF(IO.pure(Response[IO](req.context.status).withEntity(req.context.message))))
  }

  private def authAdministrator: Kleisli[IO, Request[IO], Either[AuthenticationError, Role]] = Kleisli {
    request =>
      IO.pure(for {
        header <- request.headers.get(settings.header.ci).toRight(
          Unauthorized("Couldn't find the authorization header"))
        res <- Either.cond(header.value == settings.password, Admin, Forbidden("Invalid token"))
      } yield res)
  }
}
