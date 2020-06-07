package io.mocky.http.middleware

import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }

import cats.data.Kleisli
import cats.effect.Timer
import cats.implicits._
import cats.{ Applicative, Functor }
import org.http4s._

import io.mocky.config.SleepSettings

/**
  * When the `settings.sleep.parameter` query parameter is set with a duration inferior to `settings.sleep.max-delay`,
  * the server will sleep during this duration before sending the response to the client
  */
class Sleep(settings: SleepSettings) {

  sealed private trait Delay
  private case class ValidDelay(duration: FiniteDuration) extends Delay
  private case class InvalidDelay(cause: String) extends Delay

  private object Delay {
    def parse(arg: String): Option[Delay] = {
      Try(Duration(arg)) match {
        case Success(d: FiniteDuration) if d.gteq(Duration.Zero) && d.lteq(settings.maxDelay) =>
          Some(ValidDelay(d))
        case Success(_) => Some(InvalidDelay(s"Delay must be between 0 and ${settings.maxDelay}"))
        case Failure(_) => Some(InvalidDelay("Invalid duration (expected: 10s, 100ms, 50ns"))
      }
    }
  }

  def apply[F[_]: Applicative, G[_]: Functor](http: Http[F, G])(implicit timer: Timer[F]): Http[F, G] = {
    Kleisli { req =>
      req.params.get(settings.parameter).flatMap(Delay.parse) match {
        case Some(ValidDelay(duration)) =>
          timer.sleep(duration) *> http(req)
        case Some(InvalidDelay(cause)) =>
          Response[G](Status.BadRequest).withEntity(cause).pure[F]
        case None => http(req)
      }
    }
  }

}
