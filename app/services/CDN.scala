package services

import play.api.Play.current
import controllers.routes

object CDN {

  lazy val cdnURL = current.configuration.getString("cdn.url")

  def at(file: String) = {
    val asset = routes.Assets.at(file)
    cdnURL match {
      case Some(url) => s"http://$url$asset"
      case _ => asset
    }
  }

}
