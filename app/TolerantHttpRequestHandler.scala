import play.api.http.DefaultHttpRequestHandler

import javax.inject.Inject

import play.api.http._
import play.api.i18n.MessagesApi
import play.api.mvc.RequestHeader
import play.api.routing.Router

class TolerantHttpRequestHandler @Inject() (
  errorHandler: HttpErrorHandler,
  router: Router,
  configuration: HttpConfiguration,
  filters: HttpFilters,
  messagesApi: MessagesApi)
    extends DefaultHttpRequestHandler(router, errorHandler, configuration, filters) {

  override def routeRequest(request: RequestHeader) = {
    super.routeRequest(request).orElse {
      // Authorize url with any data AFTER the version/id
      val RegexMock = """/([a-zA-Z0-9]+)/([a-z0-9]+).*""".r
      request.path match {
        case RegexMock(version, id) => Some(new _root_.controllers.Application(messagesApi).get(id, version))
        case _ => None
      }
    }
  }

}
