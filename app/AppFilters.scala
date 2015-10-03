import julienrf.play.jsonp.Jsonp

import play.api.libs.concurrent.Execution.Implicits._
import play.api.http.HttpFilters

class AppFilters extends HttpFilters {
  val filters = Seq(new Jsonp())
}
