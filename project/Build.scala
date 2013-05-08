import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "mockmyws"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.reactivemongo" %% "play2-reactivemongo" % "0.9",
    "julienrf" %% "play-jsonp-filter" % "1.0-SNAPSHOT"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "julienrf.github.com" at "http://julienrf.github.com/repo-snapshots/"
  )

}
