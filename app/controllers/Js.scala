package controllers

import javax.inject.Inject

import play.api.i18n.I18nSupport
import play.api.mvc._
import play.api.cache.Cached

import jsmessages.JsMessagesFactory

class Js @Inject()(cached: Cached, cc: ControllerComponents, jsMessagesFactory: JsMessagesFactory)
  extends AbstractController(cc) with I18nSupport {

  private val messages = jsMessagesFactory.all

  def http = cached("referentials.http") {
    Action {
      Ok(views.html.js.http.render()).as(JAVASCRIPT)
    }
  }

  def i18n = Action { implicit request =>
    Ok(messages(Some("window.I18n")))
  }

}
