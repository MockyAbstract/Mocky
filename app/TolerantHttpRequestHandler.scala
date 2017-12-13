import play.api.http.DefaultHttpRequestHandler
import javax.inject.Inject

import play.api.http._
import play.api.mvc.{Handler, RequestHeader}
import play.api.routing.Router


class TolerantHttpRequestHandler @Inject() (
  errorHandler: HttpErrorHandler,
  configuration: HttpConfiguration,
  filters: HttpFilters,
  router: Router,
  applicationController: _root_.controllers.Application
) extends DefaultHttpRequestHandler(router, errorHandler, configuration, filters) {

  // Authorize url with any data AFTER the version/id
  private val RegexMock = """/([a-zA-Z0-9]+)/([a-z0-9]+).*""".r

  override def routeRequest(request: RequestHeader): Option[Handler] = {
    super.routeRequest(request).orElse {
      request.path match {
        case RegexMock(version, id) => Some(applicationController.get(id, version))
        case _ => None
      }
    }

  }
}

