package org.janjaali.sprayjwt.akkahttp

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, ExceptionHandler, Route, RouteConcatenation}
import com.typesafe.scalalogging.LazyLogging

class ApiRoutes(routes: Route*) extends Directives with LazyLogging {
  def route: Route = {
    handleExceptions(defaultExceptionHandler) {
      pathPrefix("api" / "rest") {
          RouteConcatenation.concat(routes:_*)
      }
    }
  }

  val defaultExceptionHandler = ExceptionHandler {
    case ex: Exception =>
      logger.error(s"Catch unhandled exception in defaultExceptionHandler", ex)
      complete(StatusCodes.InternalServerError, "ups")
  }
}
