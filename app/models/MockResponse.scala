package models

import play.api.libs.json._

case class Metadata(
  status: Int,
  charset: String,
  headers: Map[String, String],
  version: String)

case class MockResponse(
  content: String,
  metadata: Metadata)

object MockResponse {
  implicit val metadataFormat = Json.format[Metadata]
  implicit val mockResponseFormat = Json.format[MockResponse]
}
