package controllers

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import javax.inject.Inject

import akka.pattern.after
import play.api.http.{ContentTypes, HeaderNames}
import play.api.i18n.{I18nSupport, Lang}
import play.api.libs.json._
import play.api.mvc._
import akka.actor.ActorSystem

import models._
import services.{IRepository, RepositoryDispatcher}

class Application @Inject()(cc: ControllerComponents, dispatcher: RepositoryDispatcher)
    (implicit ec: ExecutionContext, actorSystem: ActorSystem)
    extends AbstractController(cc) with I18nSupport {

  private val defaultError = Future(InternalServerError)

  def index = Action { implicit req =>
    val isHttps = req.headers.get(HeaderNames.X_FORWARDED_PROTO).contains("https")

    if (isHttps)
      Ok(views.html.index(Mocker.formMocker))
    else
      Redirect("https://www.mocky.io", 301)
  }

  def get(id: String, version: String) = Action.async { implicit request =>
    val repo = dispatcher(version)
    val responseFromMock = prepareMockResponse(repo, request) _

    val response = repo.getMockFromId(id).map(responseFromMock).fallbackTo(defaultError)
    addDelay(response)
  }

  private def prepareMockResponse(repo: IRepository, req: RequestHeader)(mock: MockResponse): Result = {
    val body = repo.decodeBody(mock.content, mock.metadata.charset)
    val jsonpCallback = extractJsonpParameter(req)

    // add jsonp wrapper is required
    val (bodyWithJsonP, contentType) = jsonpCallback match {
      case Some(callback) => (s"$callback($body);", ContentTypes.JAVASCRIPT)
      case None => (body, mock.metadata.contentType)
    }

    Status(mock.metadata.status)(bodyWithJsonP)
      .withHeaders(mock.metadata.headersWithoutContentType: _*)
      .as(contentType)
  }

  private def addDelay(response: Future[Result])(implicit req: RequestHeader): Future[Result] = {
    extractDelayParameter(req) match {
      case Some(duration) => after(duration, actorSystem.scheduler)(response)
      case _ => response
    }
  }

  private def extractJsonpParameter(req: RequestHeader): Option[String] = {
    req.queryString.get("callback").flatMap(_.headOption)
  }

  private def extractDelayParameter(req: RequestHeader): Option[FiniteDuration] = {
    req.queryString.get("mocky-delay").flatMap(_.headOption.map(Duration.apply)).collect {
      case d: FiniteDuration => d
    }.filter {
      case d if d.lt(Duration.Zero) => false
      case d if d.gt(60.seconds) => false
      case _ => true
    }
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
