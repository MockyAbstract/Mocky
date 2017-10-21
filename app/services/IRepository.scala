package services

import models._
import scala.concurrent.Future

trait IRepository {

  def getMockFromId(id: String): Future[MockResponse]
  def save(mock: Mocker): Future[String]

  def encodeBody(content: String, charset: String = "UTF-8"): String
  def decodeBody(content: String, charset: String = "UTF-8"): String
}
