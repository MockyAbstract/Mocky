package io.mocky.models.mocks

import java.sql.Timestamp
import java.time.{ ZoneId, ZonedDateTime }

import io.circe.Encoder
import io.circe.generic.semiauto._

final case class MockStats(createdAt: Timestamp, lastAccessAt: Option[Timestamp], totalAccess: Long)

object MockStats {
  implicit val timestampEncoder: Encoder[Timestamp] =
    Encoder.encodeZonedDateTime.contramap(ts => ZonedDateTime.from(ts.toInstant.atZone(ZoneId.of("UTC"))))
  implicit val mockStatsEncoder: Encoder.AsObject[MockStats] = deriveEncoder[MockStats]
}
