package io.mocky.utils

import java.sql.Timestamp
import java.time.{ LocalDateTime, ZoneId, ZonedDateTime }
import scala.concurrent.duration.FiniteDuration

object DateUtil {
  private val UTC = ZoneId.of("UTC")

  def now: Timestamp = Timestamp.valueOf(LocalDateTime.now())

  def future(period: FiniteDuration): ZonedDateTime = {
    ZonedDateTime.now().plusDays(period.toDays)
  }

  def toTimestamp(zdt: ZonedDateTime) = Timestamp.valueOf(zdt.withZoneSameInstant(UTC).toLocalDateTime)
}
