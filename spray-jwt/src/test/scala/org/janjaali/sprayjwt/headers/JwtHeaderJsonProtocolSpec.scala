package org.janjaali.sprayjwt.headers

import org.scalatest.funspec.AnyFunSpec
import spray.json._
import org.janjaali.sprayjwt.algorithms.Algorithms

class JwtHeaderJsonProtocolSpec extends AnyFunSpec {

  describe("JwtHeaderJsonProtocol trait") {
    it("converts JWT-Header to JsValue") {
      val jwtHeaderJson = JwtHeader(Algorithms.Hs256).toJson
      assert(
        jwtHeaderJson == JsObject(
          "typ" -> JsString("JWT"),
          "alg" -> JsString("HS256")
        )
      )
    }
  }

}
