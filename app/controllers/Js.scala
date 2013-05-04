package controllers;

import play.api._
import play.api.mvc._
import play.api.cache.Cached
import play.api.Play.current
import play.api.i18n.Messages._

// TODO: Add client cache
object Js extends Controller {

  def http = Cached("referentials.http") { Action { request =>
    Ok(views.html.js.http.render()).withHeaders(CONTENT_TYPE -> JAVASCRIPT)
  }}

  def i18n = Cached("referentials.i18n") { Action { implicit request =>
    import org.apache.commons.lang3.StringEscapeUtils.escapeEcmaScript
    val notfound = """console.warn("Key not found: " + k);"""
    val js = """var %s=(function(){var ms={%s}; var notfound=function(k){ %s return k;}; return function(k){var m=ms[k]||notfound(k);for(var i=1;i<arguments.length;i++){m=m.replace('{'+(i-1)+'}',arguments[i])} return m}})();""".format(
      "I18n",
      {
        val localizedMessages = messages.get(lang.code).getOrElse(Map.empty)
        val defaultMessages = messages.get("default").getOrElse(Map.empty)
        (for ((key, msg) <- defaultMessages ++ localizedMessages) yield {
          "'%s':'%s'".format(escapeEcmaScript(key), escapeEcmaScript(msg.replace("''", "'")))
        }).mkString(",")
      },
      if (Play.isDev) notfound else ""
    )
    Ok(js).withHeaders(CONTENT_TYPE -> JAVASCRIPT)
  }}

}
