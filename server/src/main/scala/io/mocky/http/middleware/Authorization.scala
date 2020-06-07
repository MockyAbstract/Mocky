package io.mocky.http.middleware

import cats.data.{ Kleisli, OptionT }
import cats.effect.IO
import io.chrisdavenport.log4cats.SelfAwareStructuredLogger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
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
final case class Unauthorized(message: String) extends AuthenticationError {
  val status = Status.Unauthorized
}
final case class Forbidden(message: String) extends AuthenticationError {
  val status = Status.Forbidden
}

class Authorization(settings: AdminSettings) {

  private val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  /**
    * Basic Admin authorization, working by sending a token in `settings.admin.header` header that
    * will be matched with `settings.admin.token`
    */
  val Administrator = AuthMiddleware(authAdministrator, onFailure)

  private def onFailure: AuthedRoutes[AuthenticationError, IO] = {
    Kleisli { req =>
      OptionT.liftF {
        for {
          _ <- logger.warn(s"Unauthorized access to ADMIN route: ${req.context.message} from ${req.req}")
          response <- IO.pure(Response[IO](req.context.status).withEntity(req.context.message))
        } yield response
      }
    }
  }

  private def authAdministrator: Kleisli[IO, Request[IO], Either[AuthenticationError, Role]] = Kleisli {
    request =>
      IO.pure(for {
        header <- request.headers.get(settings.header.ci).toRight(
          Unauthorized("Couldn't find the authorization header"))
        res <- Either.cond(header.value == settings.password, Admin, Forbidden(s"Invalid token '${header.value}'"))
      } yield res)
  }

}
