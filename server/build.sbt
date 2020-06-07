import Dependencies._
import com.typesafe.sbt.packager.MappingsHelper.directory

lazy val root = (project in file("."))
  .settings(
    name := "mocky-2020",
    scalaVersion := "2.13.2",
    maintainer := "yotsumi.fx+github@gmail.com",
    resolvers += Resolver.bintrayRepo("tabmo", "maven"),
    libraryDependencies ++= (http4s ++ circe ++ doobie ++ pureconfig ++ log ++ cache ++ enumeratum ++ scalatest),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
    // Allow to temporary disable Fatal-Warning (can be useful during big refactoring)
    //scalacOptions -= "-Xfatal-warnings"
  )
  // Package with resources
  .enablePlugins(JavaAppPackaging)
  .settings(mappings in Universal ++= directory("src/main/resources"))
  // Make build information available at runtime
  .enablePlugins(BuildInfoPlugin)
  .settings(Seq(
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoOptions += BuildInfoOption.BuildTime
  ))
  // Activate Integration Tests
  .configs(IntegrationTest) // Affect the same settings to integration test module
  .settings(Defaults.itSettings) // Allows to run it: tasks

// Automatically reload project when build files are modified
Global / onChangedBuildSource := ReloadOnSourceChanges

// Customize the sbt-release steps to follow gitflow process
releaseProcess := SbtReleaseProcess.steps
releaseIgnoreUntrackedFiles := true
