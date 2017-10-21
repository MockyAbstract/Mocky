package controllers

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

import play.api.http.ContentTypes
import play.api.i18n.{I18nSupport, Lang}
import play.api.libs.json._
import play.api.mvc._

import models._
import services.RepositoryDispatcher

class Application @Inject()(cc: ControllerComponents, dispatcher: RepositoryDispatcher)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with I18nSupport {

  private val defaultError = Future(InternalServerError)

  def index = Action { implicit req =>
    if (req.host == "mocky.herokuapp.com")
      Redirect("https://www.mocky.io", 301)
    else
      Ok(views.html.index(Mocker.formMocker))
  }

  def get(id: String, version: String) = Action.async { request =>
    val repo = dispatcher(version)

    val jsonpCallback = request.queryString.get("callback").flatMap(_.headOption)

    repo.getMockFromId(id).map { mock =>
      val body = repo.decodeBody(mock.content, mock.metadata.charset)

      // add jsonp wrapper is required
      val (bodyWithJsonP, contentType) = jsonpCallback match {
        case Some(callback) => (s"$callback($body);", ContentTypes.JAVASCRIPT)
        case None => (body, mock.metadata.contentType)
      }

      Status(mock.metadata.status)(bodyWithJsonP)
        .withHeaders(mock.metadata.headersWithoutContentType: _*)
        .as(contentType)
    }.fallbackTo(defaultError)
  }

  def save = Action.async { implicit request =>
    Mocker.formMocker.bindFromRequest().fold(
      error => Future.successful(BadRequest(views.html.index(error))),
      mock =>
        dispatcher.default.save(mock).map(id =>
          Ok(Json.obj("url" -> routes.Application.get(id, dispatcher.currentVersion).absoluteURL(false)))
        ).fallbackTo(defaultError)

    )
  }

  def setLang(lang: String) = Action { implicit request =>
    Redirect(routes.Application.index()).withLang(Lang(lang))
  }

}
