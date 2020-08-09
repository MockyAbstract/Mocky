package io.mocky

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import cats.effect._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.server.blaze.BlazeServerBuilder
import org.log4s._
import io.mocky.config.Config
import io.mocky.db.Database

object HttpServer {
  private val logger = getLogger

  def create(configFile: String)(implicit
    contextShift: ContextShift[IO],
    concurrentEffect: ConcurrentEffect[IO],
    timer: Timer[IO]): IO[ExitCode] = {

    Config.load(configFile).flatMap { config =>
      logger.info(s"Start app with $configFile: $config")
      resources(config)
    }.use(create)
  }

  def createFromConfig(config: Config)(implicit
    contextShift: ContextShift[IO],
    concurrentEffect: ConcurrentEffect[IO],
    timer: Timer[IO]): IO[ExitCode] = {

    resources(config).use(create)
  }

  private def resources(config: Config)(implicit contextShift: ContextShift[IO]): Resource[IO, Resources] = {
    for {
      ec <- ExecutionContexts.fixedThreadPool[IO](config.database.threadPoolSize)
      blocker <- Blocker[IO]
      transactor <- Database.transactor(config.database, ec, blocker)
    } yield Resources(transactor, config)
  }

  private def create(resources: Resources)(implicit
    concurrentEffect: ConcurrentEffect[IO],
    timer: Timer[IO],
    contextShift: ContextShift[IO]): IO[ExitCode] = {

    for {
      _ <- Database.initialize(resources.transactor)
      routing = new Routing().wire(resources)
      exitCode <-
        BlazeServerBuilder[IO](global)
          .bindHttp(resources.config.server.port, resources.config.server.host)
          .withHttpApp(routing)
          .withIdleTimeout(resources.config.settings.sleep.maxDelay.plus(5.seconds))
          .withResponseHeaderTimeout(resources.config.settings.sleep.maxDelay.plus(4.seconds))
          .serve
          .compile
          .drain
          .as(ExitCode.Success)
    } yield exitCode
  }

  final case class Resources(transactor: HikariTransactor[IO], config: Config)
}
