package io.mocky

import cats.effect.{ ExitCode, IO, IOApp }

object ServerApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val configFile = System.getenv().getOrDefault("MOCKY_CONFIGURATION_FILE", "application.conf")
    HttpServer.create(configFile)
  }
}
