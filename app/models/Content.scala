package models

import com.ning.http.util.Base64

object Content {

  def encode(content: String, charset: String) = {
    Base64.encode(("|"+content).getBytes(charset))
  }

  def decode(content: String, charset: String) = {
    new String(Base64.decode(content).drop(1), charset)
  }

}
