package io.mocky.models.mocks.actions

import io.circe.Decoder

final case class DeleteMock(secret: String)

object DeleteMock {

  implicit val deleteMockDecoder: Decoder[DeleteMock] = Decoder.instance[DeleteMock] { c =>
    for {
      secret <- c.downField("secret").as[String]
    } yield DeleteMock(secret)
  }
}
