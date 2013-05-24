package services

import scala.concurrent.Future
import models.Mocker
import models.MockResponse
import models.Metadata
import play.api.libs.json.Json
import java.io.PrintWriter
import java.util.UUID
import java.io.File
import scala.concurrent.future
import play.api.libs.concurrent.Execution.Implicits._
import scala.io.Source

object FileSystemRepository extends IRepository {

  private val outputDirectory = play.api.Play.current.configuration.getString("outputDir").getOrElse("data")

  def getMockFromId(id: String): Future[MockResponse] = {
    future {
      val fileContent = Source.fromFile(s"$outputDirectory/$id").mkString
      Json.parse(fileContent).as[MockResponse]
    }
  }

  def save(mock: Mocker): Future[String] = {
    future {
      val metadata = Metadata(mock.status, mock.charset, mock.headers, Repository.version)
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