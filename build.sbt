name := """mocky"""

version := "0.1.0"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(SbtWeb)

scalaVersion := "2.11.7"

includeFilter in (Assets, LessKeys.less) := "*.less"

libraryDependencies ++= Seq(
  ws,
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.10",
  "org.julienrf" %% "play-jsonp-filter" % "1.2",
  "org.julienrf" %% "play-jsmessages" % "2.0.0"
)

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

// Scala settings
scalacOptions ++= Seq("-encoding", "utf8") // Ensure that files are compiled in utf8
scalacOptions ++= Seq(
  "-deprecation",           // Warn when deprecated API are used
  "-feature",               // Warn for usages of features that should be importer explicitly
  "-unchecked",             // Warn when generated code depends on assumptions
  "-Ywarn-dead-code",       // Warn when dead code is identified
  "-Ywarn-numeric-widen"    // Warn when numeric are widened
)
scalacOptions in (Compile, compile) ++= Seq( // Disable for tests
  "-Xlint",                 // Additional warnings (see scalac -Xlint:help)
  "-Ywarn-adapted-args"     // Warn if an argument list is modified to match the receive
)
