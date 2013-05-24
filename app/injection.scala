package object injectedControllers {

  lazy val version = play.api.Play.current.configuration.getString("version").getOrElse("undefined")

  val Application = version match {
    case "beta" | "v1" => new controllers.Application with services.GithubRepository
    case "fs" => new controllers.Application with services.FileSystemRepository
    case _ => new controllers.Application with services.MongoRepository
  }
}