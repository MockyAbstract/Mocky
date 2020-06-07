package io.mocky.models.admin

import io.circe.Encoder
import io.circe.generic.semiauto._

case class Stats(
  nbMocks: Long,
  totalAccess: Long,
  nbMocksAccessedInMonth: Long,
  nbMocksCreatedInMonth: Long,
  nbMocksNotAccessedInYear: Long,
  nbDistinctIps: Long,
  mockAverageLength: Int
)

object Stats {
  implicit val encoderStats: Encoder.AsObject[Stats] = deriveEncoder[Stats]
}
