package controllers

import scala.concurrent.Future
import javax.inject.Inject

import play.api.i18n.{I18nSupport, Lang, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._

import models._
import services.Repository

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  private val defaultError = Future(InternalServerError)

  def index = Action { implicit req =>
    if (req.host == "mocky.herokuapp.com")
      Redirect("http://www.mocky.io", 301)
    else
      Ok(views.html.index(Mocker.formMocker))
  }

  def get(id: String, version: String) = Action.async {
    val repo = Repository(version)
    repo.getMockFromId(id).map { mock =>
      Status(mock.metadata.status)(repo.decodeBody(mock.content, mock.metadata.charset))
        .withHeaders(mock.metadata.headers.toSeq: _*)
    }.fallbackTo(defaultError)
  }

  def save = Action.async { implicit request =>
    Mocker.formMocker.bindFromRequest().fold(
      error => Future.successful(BadRequest(views.html.index(error))),
      mock =>
        Repository.current.save(mock).map(id =>
          Ok(Json.obj("url" -> routes.Application.get(id, Repository.version).absoluteURL(false)))
        ).fallbackTo(defaultError)

    )
  }

  def setLang(lang: String) = Action { implicit request =>
    Redirect(routes.Application.index()).withLang(Lang(lang))
  }

}
