package org.janjaali.sprayjwt.akkahttp.resource

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.{Directives, Route}
import org.janjaali.sprayjwt.LegacyJwt

class SecuredResourceRoute(secret: String) extends Directives {
  val routes: Route = Route {
    pathPrefix("secured" / "resource") {
      get {
        rejectEmptyResponse {
          parameter("token") { token =>
            val payload = LegacyJwt.decode(token, secret)
            complete(payload)
          }
        }
      }
    }
  }
}
