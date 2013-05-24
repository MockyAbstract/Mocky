package services

import models._
import scala.concurrent.Future

trait IRepository {

  def getMockFromId(id: String): Future[MockResponse]
  def save(mock: Mocker): Future[String]

  def encodeBody(content: String, charset: String = "UTF-8"): String
  def decodeBody(content: String, charset: String = "UTF-8"): String
}

object Repository {

  lazy val version = play.api.Play.current.configuration.getString("version").getOrElse("undefined")

  def apply(version: String) = {
    version match {
      case "beta" | "v1" => GithubRepository
      case "fs" => FileSystemRepository
      case _ => MongoRepository
    }
  }

  def current = apply(version)
}
