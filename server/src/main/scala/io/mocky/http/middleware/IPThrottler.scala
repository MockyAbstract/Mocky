package io.mocky.http.middleware

import java.util.concurrent.TimeUnit.NANOSECONDS
import scala.concurrent.duration._

import cats._
import cats.data.Kleisli
import cats.effect.concurrent.Ref
import cats.effect.{ Clock, ContextShift, IO, Sync }
import cats.implicits._
import com.github.blemale.scaffeine.{ AsyncLoadingCache, Scaffeine }
import com.typesafe.scalalogging.StrictLogging
import org.http4s._

import io.mocky.config.ThrottleSettings
import io.mocky.utils.HttpUtil

/**
  * Middleware to support wrapping json responses in jsonp.
  * Implementation based on `org.http4s.server.middleware.Jsonp`, with relaxed constraint:
  * the original content-type hasn't to be application/json`, it'll be overridden if required.
  *
  * If the wrapping is done, the response Content-Type is changed into `application/javascript`
  * and the appropriate jsonp callback is applied.
  */
object IPThrottler extends StrictLogging {

  sealed abstract class TokenAvailability extends Product with Serializable
  case object TokenAvailable extends TokenAvailability
  final case class TokenUnavailable(retryAfter: Option[FiniteDuration]) extends TokenAvailability

  /**
    * A token bucket for use with the `org.http4s.server.middleware.Throttle` middleware.
    * Consumers can take tokens which will be refilled over time.
    * Implementations are required to provide their own refill mechanism.
    *
    * Possible implementations include a remote TokenBucket service to coordinate between different application instances.
    */
  private trait TokenBucket {
    def takeToken(ip: String): IO[TokenAvailability]
  }

  private object TokenBucket {

    /**
      * Creates an in-memory `TokenBucket`.
      *
      * @param capacity the number of tokens the bucket can hold and starts with.
      * @param refillEvery the frequency with which to add another token if there is capacity spare.
      * @return A task to create the `TokenBucket`.
      */
    def local(capacity: Int, refillEvery: FiniteDuration, maxClients: Long)(implicit
      F: Sync[IO],
      clock: Clock[IO],
      cs: ContextShift[IO]): TokenBucket = {

      def getTime: IO[Long] = clock.monotonic(NANOSECONDS)

      new TokenBucket {

        private val buckets: AsyncLoadingCache[String, Ref[IO, (Double, Long)]] = Scaffeine()
          .maximumSize(maxClients)
          .buildAsyncFuture[String, Ref[IO, (Double, Long)]](
            loader = _ => getTime.flatMap(time => Ref[IO].of((capacity.toDouble, time))).unsafeToFuture()
          )

        override def takeToken(ip: String): IO[TokenAvailability] = {
          val bucket = IO.fromFuture(IO(buckets.get(ip)))

          bucket.flatMap { counter =>
            val attemptUpdate = counter.access.flatMap {
              case ((previousTokens, previousTime), setter) =>
                getTime.flatMap { currentTime =>
                  val timeDifference = currentTime - previousTime
                  val tokensToAdd = timeDifference.toDouble / refillEvery.toNanos.toDouble
                  val newTokenTotal = Math.min(previousTokens + tokensToAdd, capacity.toDouble)

                  val attemptSet: IO[Option[TokenAvailability]] = if (newTokenTotal >= 1) {
                    setter((newTokenTotal - 1, currentTime)).map(_.guard[Option].as(TokenAvailable))
                  } else {
                    val timeToNextToken = refillEvery.toNanos - timeDifference
                    val successResponse = TokenUnavailable(timeToNextToken.nanos.some)
                    setter((newTokenTotal, currentTime)).map(_.guard[Option].as(successResponse))
                  }

                  attemptSet
                }
            }

            def loop: IO[TokenAvailability] = attemptUpdate.flatMap { attempt =>
              attempt.fold(loop)(token => token.pure[IO])
            }

            loop

          }
        }
      }
    }
  }

  /**
    * Limits the supplied service to a given rate of calls using an in-memory `TokenBucket`
    *
    * @param amount the number of calls to the service to permit within the given time period.
    * @param per the time period over which a given number of calls is permitted.
    * @param maxClients number of buckets to keep in memory
    * @param http the service to transform.
    * @return a task containing the transformed service.
    */
  def apply(amount: Int, per: FiniteDuration, maxClients: Long)(
    http: Http[IO, IO])(implicit F: Sync[IO], timer: Clock[IO], cs: ContextShift[IO]): Http[IO, IO] = {

    val refillFrequency = per / amount.toLong
    val bucket = TokenBucket.local(amount, refillFrequency, maxClients)

    apply(bucket)(http)
  }

  def apply(config: ThrottleSettings)(
    http: Http[IO, IO])(implicit F: Sync[IO], timer: Clock[IO], cs: ContextShift[IO]): Http[IO, IO] = {
    apply(config.amount, config.per, config.maxClients)(http)
  }

  private def throttleResponse(retryAfter: Option[FiniteDuration], ip: String): Response[IO] = {
    logger.warn(s"Access from ip $ip throttled")
    Response[IO](Status.TooManyRequests)
      .withHeaders(Headers.of(Header("X-Retry-After", retryAfter.map(_.toString()).getOrElse("unknown"))))
  }

  /**
    * Limits the supplied service using a provided `TokenBucket`
    *
    * @param bucket a `TokenBucket` to use to track the rate of incoming requests.
    * @param http the service to transform.
    * @return a task containing the transformed service.
    */
  private def apply(bucket: TokenBucket)(http: Http[IO, IO])(implicit F: Monad[IO]): Http[IO, IO] = {
    Kleisli { req =>
      val ip = HttpUtil.getIP(req)
      bucket.takeToken(ip).flatMap {
        case TokenAvailable => http(req)
        case TokenUnavailable(retryAfter) => throttleResponse(retryAfter, ip).pure[IO]
      }
    }
  }

}
