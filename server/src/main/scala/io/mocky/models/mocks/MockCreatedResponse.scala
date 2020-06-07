package io.mocky.models.mocks

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

import io.circe.Encoder
import io.circe.generic.semiauto._

import io.mocky.models.mocks.actions.CreateUpdateMock
import io.mocky.models.mocks.feedbacks.MockCreated

case class MockCreatedResponse private (id: UUID, secret: String, expireAt: Option[ZonedDateTime], link: String)

object MockCreatedResponse {
  def apply(created: MockCreated, data: CreateUpdateMock, endpoint: String): MockCreatedResponse = {
    new MockCreatedResponse(created.id, data.secret, data.expireAt, s"$endpoint/v3/${created.id}")
  }

  implicit val zonedDTEncoder: Encoder[ZonedDateTime] =
    Encoder.encodeZonedDateTimeWithFormatter(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  implicit val mockCreatedResponseEncoder: Encoder.AsObject[MockCreatedResponse] = deriveEncoder[MockCreatedResponse]
}
