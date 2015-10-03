package controllers

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.api.cache.Cached
import jsmessages.JsMessagesFactory
import play.api.Play.current

class Js @Inject()(jsMessagesFactory: JsMessagesFactory, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  private val messages = jsMessagesFactory.all

  def http = Cached("referentials.http") { Action { request =>
    Ok(views.html.js.http.render()).withHeaders(CONTENT_TYPE -> JAVASCRIPT)
  }}

  def i18n = Action { implicit request =>
    Ok(messages(Some("window.I18n")))
  }

}
