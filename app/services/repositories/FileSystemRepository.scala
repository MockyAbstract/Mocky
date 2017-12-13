package services.repositories

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import java.io.{File, PrintWriter}
import java.util.UUID

import play.api.Configuration
import play.api.libs.json.Json

import models.{Metadata, Mocker, MockResponse}
import services.IRepository

class FileSystemRepository(currentVersion: String, config: Configuration)(implicit ec: ExecutionContext) extends IRepository {

  private val outputDirectory = config.getOptional[String]("outputDir").getOrElse("data")

  def getMockFromId(id: String): Future[MockResponse] = {
    Future {
      val fileContent = Source.fromFile(s"$outputDirectory/$id").mkString
      Json.parse(fileContent).as[MockResponse]
    }
  }

  def save(mock: Mocker): Future[String] = {
    Future {
      val metadata = Metadata(mock.status, mock.charset, mock.headers, currentVersion)
      val mockResponse = MockResponse(encodeBody(mock.body), metadata)

      val id = UUID.randomUUID().toString().replaceAll("-", "")
      new File(outputDirectory).mkdirs()
      val file = new PrintWriter(s"$outputDirectory/$id")
      try {
        file.print(Json.toJson(mockResponse))
      } finally {
        file.close
      }

      id
    }
  }

  def encodeBody(content: String, charset: String = "UTF-8"): String = content

  def decodeBody(content: String, charset: String = "UTF-8"): String = content

}
