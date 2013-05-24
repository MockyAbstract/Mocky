package controllers

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api._
import play.api.mvc._
import play.api.i18n.Lang
import play.api.Play.current
import models._
import services.IRepository

trait Application extends Controller {
  this: IRepository =>

  private val defaultError = Future(InternalServerError)

  def index = Action { implicit req =>
    if (req.host == "mocky.herokuapp.com")
      Redirect("http://www.mocky.io", 301)
    else
      Ok(views.html.index(Mocker.formMocker))
  }

  def get(id: String, version: String) = Action {
    Async {
      getMockFromId(id).map { mock =>
        Status(mock.metadata.status)(decodeBody(mock.content, mock.metadata.charset))
          .withHeaders(mock.metadata.headers.toSeq: _*)
      }.fallbackTo(defaultError)
    }
  }

  def save = Action { implicit request =>
    Mocker.formMocker.bindFromRequest().fold(
      error => BadRequest(views.html.index(error)),
      mock => Async {
        saveMock(mock).map(id =>
          Ok(Json.obj("url" -> injectedControllers.routes.Application.get(id, injectedControllers.version).absoluteURL(false)))).fallbackTo(defaultError)
      })
  }

  def setLang(lang: String) = Action { implicit request =>
    Redirect(injectedControllers.routes.Application.index()).withLang(Lang(lang))
  }

}
