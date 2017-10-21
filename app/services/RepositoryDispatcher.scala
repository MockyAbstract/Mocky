package services

import scala.concurrent.ExecutionContext
import javax.inject.Inject

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.modules.reactivemongo.ReactiveMongoApi

import services.repositories.{FileSystemRepository, GithubRepository, MongoRepository}

class RepositoryDispatcher @Inject()
    (config: Configuration, ws: WSClient, reactiveMongoApi: ReactiveMongoApi)(implicit ec: ExecutionContext) {

  val currentVersion: String = config.getOptional[String]("version").getOrElse("undefined")

  def apply(version: String): IRepository = {
    version match {
      case "beta" | "v1" => new GithubRepository(currentVersion, ws)
      case "fs" => new FileSystemRepository(currentVersion, config)
      case _ => new MongoRepository(currentVersion, reactiveMongoApi)
    }
  }

  val default: IRepository = apply(currentVersion)
}
