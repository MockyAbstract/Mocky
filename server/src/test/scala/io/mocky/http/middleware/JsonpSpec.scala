package io.mocky.http.middleware

import cats.effect.IO
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.scalamock.scalatest.MockFactory
import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class JsonpSpec extends AnyWordSpec with MockFactory with Matchers with OptionValues {

  "JSONP Filter" should {
    "accept callback with alphanumeric and some special characters" in {
      val callbacks = Seq(
        "$jQuery_1234-5678",
        "callback"
      )
      val originalBody = "alert('hello');"

      callbacks.foreach { callback =>
        val server = serveJsonp(originalBody)

        val request = Request[IO](GET, Uri.unsafeFromString(s"/?callback=$callback"))
        val response = server(request).unsafeRunSync()

        val body = response.as[String].unsafeRunSync()
        val status = response.status
        val contentType: Option[`Content-Type`] = response.contentType

        body shouldBe s"$callback($originalBody);"
        status shouldBe Ok
        contentType.value.mediaType shouldBe MediaType.application.javascript
      }

    }
  }

  private def serveJsonp(body: String): Http[IO, IO] = {
    Jsonp(HttpApp[IO] { _ => Ok(body) })
  }

}
