import sbt._

object Dependencies {

  object Versions {
    val Http4sV = "0.21.4"
    val DoobieV = "0.9.0"
    val FlywayV = "6.4.2"
    val PostgresqlV = "42.2.12"
    val CirceV = "0.13.0"
    val CirceValidationV = "0.1.1"
    val PureConfigV = "0.12.3"
    val LogbackV = "1.2.3"
    val ScalaTestV = "3.1.2"
    val ScalaMockV = "4.4.0"
    val Slf4jV = "1.7.30"
    val TestContainerV = "0.37.0"
    val EnumeratumV = "1.6.1"
  }

  import Versions._

  val http4s = Seq(
    "org.http4s" %% "http4s-blaze-server" % Http4sV,
    "org.http4s" %% "http4s-circe" % Http4sV,
    "org.http4s" %% "http4s-dsl" % Http4sV,
    "org.http4s" %% "http4s-blaze-client" % Http4sV % "it,test"
  )

  val doobie = Seq(
    "org.tpolecat" %% "doobie-core" % DoobieV,
    "org.tpolecat" %% "doobie-hikari" % DoobieV,
    "org.tpolecat" %% "doobie-postgres" % DoobieV,
    "org.tpolecat" %% "doobie-postgres-circe" % DoobieV,
    "org.postgresql" % "postgresql" % PostgresqlV,
    "org.flywaydb" % "flyway-core" % FlywayV
  )

  val circe = Seq(
    "io.circe" %% "circe-generic" % CirceV,
    "io.circe" %% "circe-literal" % CirceV % "it,test",
    "io.circe" %% "circe-optics" % CirceV % "it,test",
    "io.tabmo" %% "circe-validation-core" % CirceValidationV,
    "io.tabmo" %% "circe-validation-extra-rules" % CirceValidationV
  )

  val pureconfig = Seq(
    "com.github.pureconfig" %% "pureconfig" % PureConfigV,
    "com.github.pureconfig" %% "pureconfig-cats-effect" % PureConfigV
  )

  val scalatest = Seq(
    "org.scalatest" %% "scalatest" % ScalaTestV % "it,test",
    "org.scalamock" %% "scalamock" % ScalaMockV % Test,
    "com.dimafeng" %% "testcontainers-scala-scalatest" % TestContainerV % IntegrationTest,
    "com.dimafeng" %% "testcontainers-scala-postgresql" % TestContainerV % IntegrationTest
  )

  val log = Seq(
    "ch.qos.logback" % "logback-classic" % LogbackV,
    "org.slf4j" % "slf4j-api" % Slf4jV,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "io.chrisdavenport" %% "log4cats-slf4j" % "1.1.1"
  )

  val cache = Seq(
    "com.github.blemale" %% "scaffeine" % "4.0.0" % Compile
  )

  val enumeratum = Seq(
    "com.beachape" %% "enumeratum" % EnumeratumV,
    "com.beachape" %% "enumeratum-circe" % EnumeratumV
  )

}
