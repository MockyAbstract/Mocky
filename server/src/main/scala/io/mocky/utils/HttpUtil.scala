package io.mocky.utils

import org.http4s.Request
import org.http4s.util.CaseInsensitiveString

object HttpUtil {

  private val SOURCE_IP_1 = CaseInsensitiveString("X-Real-Ip")
  private val SOURCE_IP_2 = CaseInsensitiveString("X-Forwarded-For")
  private val SOURCE_IP_3 = CaseInsensitiveString("HTTP_CLIENT_IP")
  private val SOURCE_IP_4 = CaseInsensitiveString("REMOTE_ADDR")

  def getIP[F[_]](req: Request[F]): String = {
    val headers = req.headers

    headers.get(SOURCE_IP_1).map(_.value)
      .orElse(headers.get(SOURCE_IP_2).map(_.value))
      .orElse(headers.get(SOURCE_IP_3).map(_.value))
      .orElse(headers.get(SOURCE_IP_4).map(_.value))
      .getOrElse("0.0.0.0")
  }
}
