package io.mocky.http.middleware

import scala.concurrent.duration._

import cats.effect.IO
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class IPThrottlerSpec extends AnyWordSpec with MockFactory with Matchers {
  implicit val ec = scala.concurrent.ExecutionContext.global
  implicit val contextShift = IO.contextShift(ec)
  implicit val timer = IO.timer(ec)

  "Http Server" should {
    "accept request coming in a slow rate" in {
      val server = serverWithThrottling(5, 1.seconds)
      val results = for {
        _ <- assert("r1", true, sendManyRequestInParallel(server, 3))
        _ <- timer.sleep(1.seconds)
        _ <- assert("r2", true, sendManyRequestInParallel(server, 5))
        _ <- timer.sleep(1.seconds)
        _ <- assert("r3", true, sendManyRequestInParallel(server, 4))
      } yield 1

      results.unsafeRunSync()
      succeed
    }

    "accept high volume requests" in {
      val server = serverWithThrottling(1000, 1.seconds)
      val results = for {
        _ <- assert("r1", true, sendManyRequestInParallel(server, 1000))
        _ <- timer.sleep(1100.millis)
        _ <- assert("r2", true, sendManyRequestInParallel(server, 1000))
        _ <- timer.sleep(1100.millis)
        _ <- assert("r3", true, sendManyRequestInParallel(server, 1000))
      } yield 1

      results.unsafeRunSync()
      succeed
    }

    "allow multiple clients with their own throttling rate" in {
      val server = serverWithThrottling(100, 1.seconds)
      val results = for {
        _ <- assert("r1a", true, sendManyRequestInParallel(server, 100, "ip1"))
        _ <- assert("r1a", true, sendManyRequestInParallel(server, 100, "ip2"))
        _ <- timer.sleep(1100.millis)
        _ <- assert("r1b", true, sendManyRequestInParallel(server, 100, "ip1"))
        _ <- assert("r1b", true, sendManyRequestInParallel(server, 100, "ip2"))
        _ <- timer.sleep(1100.millis)
        _ <- assert("r1c", true, sendManyRequestInParallel(server, 100, "ip1"))
        _ <- assert("r1c", true, sendManyRequestInParallel(server, 100, "ip2"))
      } yield 1

      results.unsafeRunSync()
      succeed
    }

    "respond with an error if the rate exceed" in {
      val server = serverWithThrottling(5, 1.seconds)
      val results = assert("r", false, sendManyRequestInParallel(server, 10))

      results.unsafeRunSync()
      succeed
    }

    "respond with an error for one client while other clients have valid responses" in {
      val server = serverWithThrottling(5, 1.seconds)
      val results = for {
        _ <- assert("r1a", false, sendManyRequestInParallel(server, 10, "ip1"))
        _ <- assert("r2a", true, sendManyRequestInParallel(server, 5, "ip2"))
        _ <- assert("r2a", true, sendManyRequestInParallel(server, 5, "ip3"))
        _ <- timer.sleep(1.second)
        _ <- assert("r1b", true, sendManyRequestInParallel(server, 5, "ip1"))
        _ <- assert("r1b", true, sendManyRequestInParallel(server, 5, "ip2"))
        _ <- assert("r1b", true, sendManyRequestInParallel(server, 5, "ip3"))
        _ <- timer.sleep(1.second)
        _ <- assert("r1c", true, sendManyRequestInParallel(server, 5, "ip1"))
        _ <- assert("r1c", true, sendManyRequestInParallel(server, 5, "ip2"))
        _ <- assert("r1c", false, sendManyRequestInParallel(server, 10, "ip3"))
        _ <- timer.sleep(1.second)
        _ <- assert("r1d", false, sendManyRequestInParallel(server, 10, "ip1"))
        _ <- assert("r1d", true, sendManyRequestInParallel(server, 5, "ip2"))
        _ <- assert("r1d", false, sendManyRequestInParallel(server, 10, "ip3"))
      } yield 1

      results.unsafeRunSync()
      succeed
    }

    "allow to long series of requests that are really close to the limit with max clients" in {
      val durationSeconds = 30
      val players = 300

      val server = serverWithThrottling(5, 1.seconds, players.toLong)

      def fivePerSecond(run: Int, player: Int) = runNRequestEvery(server, run, player, 1, 201.millis, true)

      val globalResult = 1.to(players).map { player =>
        (1.to(durationSeconds).map(run => fivePerSecond(run, player))).toList.sequence
      }.toList.parSequence

      globalResult.unsafeRunSync()
      succeed
    }

    "will not work as expected with to many clients" ignore {
      // In this test each client will send 10 req/second with a throttle to 5 req/s. So each batch is expected to fail.
      // But we'll run the test with 200 clients with a bucket allowing only 5 concurrent clients
      // so the bucket will be renewed regularly and we expect that the majority of batch will succeed

      val rounds = 30
      val players = 200

      val server = serverWithThrottling(5, 1.seconds, 5L)

      def tenPerSecond(player: Int) = runNRequestEvery(server, player, 10, 100.millis)

      val globalResult = 1.to(players).map { player =>
        (1.to(rounds).map(_ => tenPerSecond(player))).toList.sequence
      }.toList.parSequence

      val res = globalResult.unsafeRunSync().flatten
      val batchsOK = res.count(_ == true)
      val batchsNOK = res.count(_ == false)
      val total = batchsOK + batchsNOK
      val okRatio = batchsOK.toFloat / total

      okRatio should be >= 0.1f
    }
  }

  private def sendManyRequestInParallel(server: Http[IO, IO], n: Int, ip: String = "1.1.1.1"): IO[Boolean] = {
    val request = Request[IO](GET, uri"/").withHeaders(Header("REMOTE_ADDR", ip))
    0.until(n).map(_ => server(request)).toList.parSequence
      .map(_.forall(_.status == Ok))
  }

  private def assert(name: String, expected: Boolean, task: IO[Boolean]): IO[Boolean] = {
    task.ensure(new RuntimeException(s"$name was expected $expected but not"))(res => res == expected)
  }

  private def serverWithThrottling(amount: Int, per: FiniteDuration, maxClients: Long = 10000): Http[IO, IO] = {
    IPThrottler(amount, per, maxClients)(HttpApp[IO] { _ => Ok() })
  }

  private def runNRequestEvery(svr: Http[IO, IO], meta: Int, ip: Int, n: Int, delay: FiniteDuration, expect: Boolean) =
    for {
      _ <- assert(s"r${meta}-${ip}", expect, sendManyRequestInParallel(svr, n, ip.toString))
      _ <- timer.sleep(delay)
    } yield 1

  private def runNRequestEvery(server: Http[IO, IO], ip: Int, n: Int, delay: FiniteDuration): IO[Boolean] = for {
    res <- sendManyRequestInParallel(server, n, ip.toString)
    _ <- timer.sleep(delay)
  } yield res

}
