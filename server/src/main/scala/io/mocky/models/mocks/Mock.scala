package io.mocky.models.mocks

import io.circe.generic.semiauto._
import io.circe.{ Encoder, Json }

final case class Mock(
  content: Option[Array[Byte]],
  status: Int,
  contentType: String,
  charset: String,
  headers: Option[Json])

object Mock {
  implicit val mockEncoder: Encoder.AsObject[Mock] = deriveEncoder[Mock]
}
