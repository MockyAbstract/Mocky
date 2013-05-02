package controllers

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import scala.concurrent.Future
import play.api._
import play.api.mvc._

import models._
import models.Gist._

object Application extends Controller {

  private val defaultError = Future(InternalServerError)

  def index = Action {
    Ok(views.html.index(Mocker.formMocker))
  }

  def get(id: String, version: String) = Action {
    val data = for {
      gist <- Gist.get(id)
      content <- gist.fetchContent()
      metadata <- gist.fetchMetadata()
    } yield (content, metadata)

    Async {
      data.map {
        case (content, metadata) =>
          Status(metadata.status)(Content.decode(content, metadata.charset))
            .withHeaders(metadata.headers.toSeq: _*)
      }.fallbackTo(defaultError)
    }
  }

  def save = Action { implicit request =>
    Mocker.formMocker.bindFromRequest().fold(
      error => BadRequest(views.html.index(error)),
      mock => Async {
        Gist.create(mock).map(res =>
          Ok(Json.obj("url" -> routes.Application.get(res.id, Mocker.version).absoluteURL(false)))
        ).fallbackTo(defaultError)
      }
    )
  }

}
