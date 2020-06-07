import java.time.ZonedDateTime
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

import cats.effect.{ ContextShift, IO, Timer }
import com.dimafeng.testcontainers.{ ForAllTestContainer, PostgreSQLContainer }
import io.circe.Json
import io.circe.optics.JsonPath._
import org.http4s.circe._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.util.{ CaseInsensitiveString => IString }
import org.http4s.{ Header, Request, Status, Uri }
import org.scalatest.OptionValues
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.wordspec.AnyWordSpec

import io.mocky.HttpServer
import io.mocky.config._

class MockyServerSpec extends AnyWordSpec with Matchers with OptionValues with Eventually with ForAllTestContainer {

  implicit private val timer: Timer[IO] = IO.timer(ExecutionContext.global)
  implicit private val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit override val patienceConfig: PatienceConfig = PatienceConfig().copy(timeout = scaled(Span(5, Seconds)))

  private lazy val client = BlazeClientBuilder[IO](global).resource
  override val container = PostgreSQLContainer(dockerImageNameOverride = "postgres:12")

  private val serverPort = Random.nextInt(40000) + 1024
  private lazy val URL = s"http://localhost:$serverPort"

  override def afterStart(): Unit = {
    val config = Config(
      server = ServerConfig("localhost", serverPort),
      database = DatabaseConfig(
        driver = container.driverClassName,
        url = container.jdbcUrl,
        user = container.username,
        password = container.password
      ),
      settings = Settings(
        environment = "dev",
        endpoint = "https://mocky.io",
        mock = MockSettings(1000000, 1000),
        security = SecuritySettings(14),
        throttle = ThrottleSettings(100, 1.seconds, 10000),
        sleep = SleepSettings(60.seconds, "mocky-delay"),
        cors = CorsSettings("mocky.io"),
        admin = AdminSettings("X-Auth-Token", "secret")
      )
    )

    // Launch the mocky server
    HttpServer.createFromConfig(config).unsafeRunAsyncAndForget()

    // Wait for the server to be available
    val _ = eventually {
      client.use(_.statusFromUri(Uri.unsafeFromString(s"$URL/api/status"))).unsafeRunSync() shouldBe Status.Ok
    }
  }

  "Mocky server" should {

    "play an UTF8 mock" in {
      val id = "48e9c41b-de8c-4aeb-99a8-2f3abe3e5efa" // from V003 migration

      val request = Request[IO](uri = Uri.unsafeFromString(s"$URL/v3/$id"))

      val response = client.use(_.expect[String](request)).unsafeRunSync()
      response shouldBe
        "Dès Noël, où un zéphyr haï me vêt de glaçons würmiens, je dîne d’exquis rôtis de bœuf au kir, à l’aÿ d’âge mûr, &cætera."

      val headers = client.use(_.fetch(request)(resp => IO.pure(resp.headers))).unsafeRunSync()
      headers.get(IString("X-SAMPLE-HEADER")).value.value shouldBe "Sample value"
      headers.get(IString("Content-TYPE")).value.value shouldBe "text/plain; charset=UTF-8"

      val status = client.use(_.status(request)).unsafeRunSync()
      status shouldBe Status.Created
    }

    "play an ISO-8859-1 mock" in {
      val id = "6c23b606-29a7-4e0f-9343-87ec0a8ac5e5" // from V003 migration
      val request = Request[IO](uri = Uri.unsafeFromString(s"$URL/v3/$id"))

      val response = client.use(_.expect[String](request)).unsafeRunSync()
      response shouldBe
        "Dès Noël, où un zéphyr haï me vêt de glaçons würmiens, je dîne d'exquis rôtis de boeuf au kir, à l'aÿ d'âge mûr, &cætera."

      val headers = client.use(_.fetch(request)(resp => IO.pure(resp.headers))).unsafeRunSync()
      headers.get(IString("X-SAMPLE-HEADER")).value.value shouldBe "Sample value"
      headers.get(IString("Content-TYPE")).value.value shouldBe "text/plain; charset=ISO-8859-1"

      val status = client.use(_.status(request)).unsafeRunSync()
      status shouldBe Status.Ok
    }

    "stats must be updated after each play" in {
      val id = "c7b8ba84-19da-4f51-bbca-25ef1e4bb3da" // from V003 migration
      val call = Request[IO](uri = Uri.unsafeFromString(s"$URL/v3/$id"))

      // Call the mock multiple times
      val nbCalls = 35
      0.until(nbCalls).foreach(_ => client.use(_.expect[String](call)).unsafeRunSync())

      val requestStats = Request[IO](uri = Uri.unsafeFromString(s"$URL/api/mock/$id/stats"))
      val stats = client.use(_.expect[Json](requestStats)).unsafeRunSync()

      val totalAccess = root.totalAccess.int.getOption(stats).value
      val lastAccessAt = root.lastAccessAt.as[ZonedDateTime].getOption(stats).value

      val initialCounter = 1000 // from SQL migration
      totalAccess shouldBe initialCounter + nbCalls

      lastAccessAt.isAfter(ZonedDateTime.now().minusSeconds(20)) shouldBe true
      lastAccessAt.isBefore(ZonedDateTime.now()) shouldBe true
    }

    "refuse access to Admin route without token" in {
      val request = Request[IO](uri = Uri.unsafeFromString(s"$URL/admin/api/stats"))

      val status = client.use(_.status(request)).unsafeRunSync()
      status shouldBe Status.Unauthorized
    }

    "refuse access to Admin route with wrong token" in {
      val request = Request[IO](uri = Uri.unsafeFromString(s"$URL/admin/api/stats"))
        .withHeaders(Header("X-Auth-Token", "wrongsecret"))

      val status = client.use(_.status(request)).unsafeRunSync()
      status shouldBe Status.Forbidden
    }

    "authorize access to Admin route with the right token" in {
      val request = Request[IO](uri = Uri.unsafeFromString(s"$URL/admin/api/stats"))
        .withHeaders(Header("X-Auth-Token", "secret"))

      val status = client.use(_.status(request)).unsafeRunSync()
      status shouldBe Status.Ok
    }

  }

}
