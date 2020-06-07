package io.mocky.services

import cats.effect.IO
import io.circe.Json
import io.circe.literal._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.headers._
import org.http4s.implicits._
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import io.mocky.data.Fixtures
import io.mocky.http.middleware.Jsonp
import io.mocky.models.mocks.MockResponse
import io.mocky.repositories.{ MockV2Repository, MockV3Repository }

class MockRunnerServiceSpec extends AnyWordSpec with MockFactory with Matchers {
  implicit val ec = scala.concurrent.ExecutionContext.global
  implicit val timer = IO.timer(ec)

  private val repositoryV2 = stub[MockV2Repository]
  private val repositoryV3 = stub[MockV3Repository]
  private val settings = Fixtures.settings
  private val service = new MockRunnerService(repositoryV2, repositoryV3, settings)
  private val routes = service.routing

  "Mock Runner" should {

    val id = java.util.UUID.randomUUID()
    val content = json"""{"hello":"world"}""".noSpaces
    val header = Header("X-Foo-Bar", "HelloWorld")
    val mock = MockResponse(
      content = Some(content.getBytes(MockResponse.DEFAULT_CHARSET.nioCharset)),
      charset = MockResponse.DEFAULT_CHARSET,
      status = Status.Created,
      contentType = MockResponse.DEFAULT_CONTENT_TYPE.withCharset(MockResponse.DEFAULT_CHARSET),
      headers = Headers.of(header)
    )

    val verbs: Seq[Method] = Seq(GET, POST, PUT, PATCH, DELETE, TRACE)

    verbs.foreach { verb =>
      s"play a mock with $verb request" in {
        (repositoryV3.touchAndGetMockResponse _).when(id).returns(IO.pure(Right(mock)))
        val response = serve(Request[IO](verb, Uri.unsafeFromString(s"/v3/$id")))

        response.status shouldBe Status.Created
        response.as[Json].map(_.noSpaces).unsafeRunSync() shouldBe content
        response.headers.get(header.name) shouldBe Some(header)
      }
    }

    "support the sleep feature" in {
      (repositoryV3.touchAndGetMockResponse _).when(id).returns(IO.pure(Right(mock)))

      val delay = 1500L
      val start = System.currentTimeMillis()
      val _ = serve(Request[IO](GET, Uri.unsafeFromString(s"/v3/$id?${settings.sleep.parameter}=${delay}ms")))
      val end = System.currentTimeMillis()

      (end - start) shouldBe (delay +- 100)
    }

    "support the callback feature" in {
      (repositoryV3.touchAndGetMockResponse _).when(id).returns(IO.pure(Right(mock)))

      val response = serve(Request[IO](GET, Uri.unsafeFromString(s"/v3/$id?${Jsonp.DEFAULT_PARAMETER}=wrapInside")))

      response.status shouldBe Status.Created
      response.as[String].unsafeRunSync() shouldBe s"wrapInside($content);"
      response.headers.get(`Content-Type`) shouldBe Some(`Content-Type`(MediaType.application.javascript))
    }

    "support CORS request coming from any origin, any header, etc" in {

      (repositoryV3.touchAndGetMockResponse _).when(id).returns(IO.pure(Right(mock)))

      val origin = "http://www.anywebsite.fr"
      val corsRequestHeaders = Seq(
        `Origin`.parse(origin),
        `Access-Control-Request-Method`.parse("DELETE"),
        `Access-Control-Request-Headers`.parse("X-FOO-BAR, Authorization2")
      ).map(_.toOption.get)

      def headersMap(response: Response[IO]) = response.headers.toList.view.map(h => h.name -> h.value).to(Map)

      // Check preflight flow
      {
        val preflightRequest = Request[IO](OPTIONS, Uri.unsafeFromString(s"/v3/$id"))
          .withHeaders(corsRequestHeaders: _*)
        val response = serve(preflightRequest)
        val headers = headersMap(response)

        response.status shouldBe Status.Ok

        headers("Access-Control-Allow-Origin".ci) shouldBe origin
        headers("Access-Control-Allow-Methods".ci) shouldBe "DELETE"
        headers("Access-Control-Allow-Headers".ci).contains("*") shouldBe true
        headers("Access-Control-Max-Age".ci) should not be empty
      }

      // Check main flow
      {
        val request = Request[IO](DELETE, Uri.unsafeFromString(s"/v3/$id")).withEntity("Hello")
          .withHeaders(corsRequestHeaders: _*)
        val response = serve(request)
        val headers = headersMap(response)

        response.status shouldBe Status.Created

        headers("Access-Control-Allow-Origin".ci) shouldBe origin
        headers("X-FOO-BAR".ci) shouldBe header.value
        headers("Content-Type".ci) shouldBe mock.contentType.value

      }

    }

  }

  private def serve(request: Request[IO]): Response[IO] = {
    routes.orNotFound(request).unsafeRunSync()
  }
}
