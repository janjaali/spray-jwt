package org.janjaali.sprayjwt.akkahttp.token

import akka.http.scaladsl.server.{Directives, Route}
import org.janjaali.sprayjwt.Jwt
import org.janjaali.sprayjwt.algorithms.HS256
import spray.json.{JsObject, JsString}

class TokenRoutes(secret: String) extends Directives {
  val routes: Route = Route {
    path("token") {
      rejectEmptyResponse {
        get {
          val payload = JsObject("dance" -> JsString("in the rain"))
          val jwt = Jwt.encode(payload, secret, HS256)
          complete(jwt)
        }
      }
    }
  }
}
