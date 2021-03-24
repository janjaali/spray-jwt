package org.janjaali.sprayjwt.akkahttp.token

import akka.http.scaladsl.server.{Directives, Route}
import org.janjaali.sprayjwt.LegacyJwt
import spray.json.{JsObject, JsString}
import org.janjaali.sprayjwt.algorithms.Algorithm

class TokenRoutes(secret: String) extends Directives {
  val routes: Route = Route {
    path("token") {
      rejectEmptyResponse {
        get {
          val payload = JsObject("dance" -> JsString("in the rain"))
          val jwt = LegacyJwt.encode(payload, secret, Algorithm.Hmac.Hs256)
          complete(jwt)
        }
      }
    }
  }
}
