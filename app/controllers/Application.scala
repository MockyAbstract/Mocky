package controllers

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api._
import play.api.mvc._
import play.api.i18n.Lang
import play.api.Play.current

import models._
import services.{Repository, MongoRepository, GithubRepository}


object Application extends Controller {

  private val defaultError = Future(InternalServerError)
  lazy val assetVersion = current.configuration.getString("assets.version").getOrElse("")

  def index = Action { implicit req =>
    Ok(views.html.index(Mocker.formMocker))
  }

  def get(id: String, version: String) = Action {
    Async {
      val repo = Repository(version)
      repo.getMockFromId(id).map { mock =>
        Status(mock.metadata.status)(repo.decodeBody(mock.content, mock.metadata.charset))
          .withHeaders(mock.metadata.headers.toSeq: _*)
      }.fallbackTo(defaultError)
    }
  }

  def save = Action { implicit request =>
    Mocker.formMocker.bindFromRequest().fold(
      error => BadRequest(views.html.index(error)),
      mock => Async {
        Repository.current.save(mock).map(id =>
          Ok(Json.obj("url" -> routes.Application.get(id, Repository.version).absoluteURL(false)))
        ).fallbackTo(defaultError)
      }
    )
  }

  def setLang(lang: String) = Action { implicit request =>
    Redirect(routes.Application.index()).withLang(Lang(lang))
  }

}
