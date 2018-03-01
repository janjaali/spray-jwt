package org.janjaali.sprayjwt.akkahttp.token

import akka.http.scaladsl.server.{Directives, Route}

class TokenRoutes extends Directives {
  val routes: Route = Route {
    path("token") {
      get {
        complete("dance")
      }
    }
  }
}
