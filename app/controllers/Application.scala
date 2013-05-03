package controllers

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api._
import play.api.mvc._
import play.api.i18n.Lang
import play.api.Play.current

import models._
import services.{MongoRepository, GithubRepository}


object Application extends Controller {

  private val defaultError = Future(InternalServerError)
  lazy val assetVersion = current.configuration.getString("assets.version").getOrElse("")

  def index = Action { implicit req =>
    Ok(views.html.index(Mocker.formMocker))
  }

  def get(id: String, version: String) = Action {
    Async {
      getRepository(version).getMockFromId(id).map { mock =>
        Status(mock.metadata.status)(ContentUtil.decode(mock.content, mock.metadata.charset))
          .withHeaders(mock.metadata.headers.toSeq: _*)
      }.fallbackTo(defaultError)
    }
  }

  def save = Action { implicit request =>
    Mocker.formMocker.bindFromRequest().fold(
      error => BadRequest(views.html.index(error)),
      mock => Async {
        MongoRepository.save(mock).map(id =>
          Ok(Json.obj("url" -> routes.Application.get(id, Mocker.version).absoluteURL(false)))
        ).fallbackTo(defaultError)
      }
    )
  }

  def setLang(lang: String) = Action { implicit request =>
    Redirect(routes.Application.index()).withLang(Lang(lang))
  }

  private def getRepository(version: String) = {
    version match {
      case "beta" | "v1" => GithubRepository
      case _ => MongoRepository
    }
  }

}
