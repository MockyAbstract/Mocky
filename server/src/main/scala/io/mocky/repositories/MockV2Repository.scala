package io.mocky.repositories

import scala.annotation.nowarn

import cats.effect.IO
import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.postgres.circe.jsonb.implicits._
import doobie.util.log.LogHandler

import io.mocky.http.middleware.Admin
import io.mocky.models.Gate
import io.mocky.models.admin.Stats
import io.mocky.models.errors.MockNotFoundError
import io.mocky.models.mocks.{ Mock, MockResponse }
import io.mocky.utils.DateUtil

/**
  * Load legacy mocks from PG, that have been migrated from MongoDB
  */
class MockV2Repository(transactor: Transactor[IO]) {

  implicit val log: LogHandler = LogHandler.jdkLogHandler

  private object SQL {
    private val TABLE = Fragment.const("mocks_v2")

    def GET(id: String): Fragment =
      fr"SELECT content, status, content_type, charset, headers FROM $TABLE WHERE id = $id"

    def UPDATE_STATS(id: String): Fragment =
      fr"UPDATE $TABLE SET last_access_at = ${DateUtil.now}, total_access = total_access + 1 WHERE id = $id"

    val ADMIN_STATS: Fragment =
      fr"""
          SELECT 
            COUNT(*) as nb_mocks,
            SUM(total_access) as total_access,
            SUM(case when last_access_at > NOW() - INTERVAL '1 MONTH' then 1 else 0 end) as nb_mocks_accessed_in_month,
            -1 as nb_mocks_created_in_month,
            SUM(case when last_access_at IS NULL then 1 else 0 end) as nb_mocks_never_accessed,
            SUM(case when last_access_at IS NULL OR  last_access_at < NOW() - INTERVAL '1 YEAR' then 1 else 0 end) as nb_mocks_not_accessed_in_year,
            -1 as nb_distinct_ips,
            ROUND(AVG(OCTET_LENGTH(content))) as mock_average_length
          FROM $TABLE
      """
  }

  /**
    * Fetch a legacy mock by its primary key, and update its stats
    */
  def touchAndGetMockResponse(id: String): IO[Either[MockNotFoundError.type, MockResponse]] = {
    val queries = for {
      mock <- SQL.GET(id).query[Mock].option
      _ <- SQL.UPDATE_STATS(id).update.run
    } yield mock

    queries.transact(transactor).map {
      case Some(mock) => Right(MockResponse(mock))
      case None => Left(MockNotFoundError)
    }
  }

  /**
    * Return some global statistics about V3 mocks
    * @param admin Gate to restrict this  action to admin only
    */
  def adminStats()(implicit @nowarn admin: Gate[Admin.type]): IO[Stats] = {
    SQL.ADMIN_STATS.query[Stats].unique.transact(transactor)
  }
}
