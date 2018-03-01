package org.janjaali.sprayjwt.akkahttp

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, ExceptionHandler, Route}
import com.typesafe.scalalogging.LazyLogging

class ApiRoutes extends Directives with LazyLogging {
  def routes: Route = {
    handleExceptions(defaultExceptionHandler) {
      complete("dance")
    }
  }

  val defaultExceptionHandler = ExceptionHandler {
    case ex: Exception =>
      logger.error(s"Catch unhandled exception in defaultExceptionHandler", ex)
      complete(StatusCodes.InternalServerError, "ups")
  }
}
