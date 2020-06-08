package io.mocky.http.middleware

import java.nio.charset.StandardCharsets

import cats.data.Kleisli
import cats.implicits._
import cats.{ Applicative, Functor }
import fs2.Chunk
import fs2.Stream._
import org.http4s._
import org.http4s.headers._

/**
  * Middleware to support wrapping json responses in jsonp.
  * Implementation based on `org.http4s.server.middleware.Jsonp`, with relaxed constraint:
  * the original content-type hasn't to be `application/json`, it'll be overridden if required.
  *
  * If the wrapping is done, the response Content-Type is changed into `application/javascript`
  * and the appropriate jsonp callback is applied.
  */
object Jsonp {

  val DEFAULT_PARAMETER = "callback"

  // A regex to match a valid javascript function name to shield the client from some jsonp related attacks
  private val ValidCallback =
    """^((?!(?:do|if|in|for|let|new|try|var|case|else|enum|eval|false|null|this|true|void|with|break|catch|class|const|super|throw|while|yield|delete|export|import|public|return|static|switch|typeof|default|extends|finally|package|private|continue|debugger|function|arguments|interface|protected|implements|instanceof)$)[$A-Z_a-z-﹏0-９＿]*)$""".r

  def apply[F[_]: Applicative, G[_]: Functor](http: Http[F, G]): Http[F, G] = apply(DEFAULT_PARAMETER)(http)

  def apply[F[_]: Applicative, G[_]: Functor](callbackParam: String)(http: Http[F, G]): Http[F, G] =
    Kleisli { req =>
      req.params.get(callbackParam) match {
        case Some(ValidCallback(callback)) =>
          http.map(jsonp(_, callback)).apply(req)
        case Some(invalidCallback @ _) =>
          Response[G](Status.BadRequest).withEntity(s"Not a valid callback name.").pure[F]
        case None => http(req)
      }
    }

  private def jsonp[F[_]: Functor](resp: Response[F], callback: String) = {
    val begin = beginJsonp(callback)
    val end = EndJsonp
    val jsonpBody = chunk(begin) ++ resp.body ++ chunk(end)

    val newHeaders = resp.headers.get(`Content-Length`) match {
      case None => resp.headers
      case Some(oldContentLength) =>
        resp.headers
          .filterNot(_.is(`Content-Length`))
          .put(`Content-Length`.unsafeFromLong(oldContentLength.length + begin.size + end.size))
    }

    resp
      .copy(body = jsonpBody)
      .withHeaders(newHeaders)
      .withContentType(`Content-Type`(MediaType.application.javascript))
  }

  private def beginJsonp(callback: String) =
    Chunk.bytes((callback + "(").getBytes(StandardCharsets.UTF_8))

  private val EndJsonp =
    Chunk.bytes(");".getBytes(StandardCharsets.UTF_8))
}
