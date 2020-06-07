package io.mocky

import cats.effect.{ ContextShift, IO, Timer }
import cats.implicits._
import org.http4s.Http
import org.http4s.implicits._
import org.http4s.server.Router

import io.mocky.HttpServer.Resources
import io.mocky.config.ThrottleSettings
import io.mocky.http.middleware.IPThrottler
import io.mocky.repositories.{ MockV2Repository, MockV3Repository }
import io.mocky.services._

class Routing {

  def wire(resources: Resources)(implicit timer: Timer[IO], contextShift: ContextShift[IO]): Http[IO, IO] = {
    val repositoryV2 = new MockV2Repository(resources.transactor)
    val repositoryV3 = new MockV3Repository(resources.transactor, resources.config.settings.security)

    val mockApiService = new MockApiService(repositoryV3, resources.config.settings)
    val mockAdminApiService = new MockAdminApiService(repositoryV2, repositoryV3, resources.config.settings)
    val mockRunnerService = new MockRunnerService(repositoryV2, repositoryV3, resources.config.settings)
    val designerService = new DesignerService(resources.config.settings.cors)
    val statusService = new StatusService()

    val throttleConfig = resources.config.settings.throttle

    routes(mockApiService, mockAdminApiService, mockRunnerService, statusService, designerService, throttleConfig)
  }

  def routes(
    mockApiService: MockApiService,
    mockAdminApiService: MockAdminApiService,
    mockRunnerService: MockRunnerService,
    statusService: StatusService,
    designerService: DesignerService,
    throttleConfig: ThrottleSettings)(implicit timer: Timer[IO], contextShift: ContextShift[IO]): Http[IO, IO] = {

    val mockRoutes = mockRunnerService.routing
    val apiRoutes = statusService.routes <+> mockApiService.routing
    val designerRoutes = designerService.routes
    val adminRoutes = mockAdminApiService.routing

    val allRoutes = Router(
      "/admin" -> adminRoutes,
      "/" -> (designerRoutes <+> mockRoutes <+> apiRoutes)
    ).orNotFound

    val throttledRoutes = IPThrottler(throttleConfig)(allRoutes)

    throttledRoutes
  }

}
