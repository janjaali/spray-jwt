package org.janjaali.sprayjwt.headers

import org.janjaali.sprayjwt.algorithms.HS256
import org.scalatest.funspec.AnyFunSpec
import spray.json._

class JwtHeaderJsonProtocolSpec extends AnyFunSpec {

  describe("JwtHeaderJsonProtocol trait") {
    it("converts JWT-Header to JsValue") {
      val jwtHeaderJson = JwtHeader(HS256).toJson
      assert(jwtHeaderJson == JsObject(
        "typ" -> JsString("JWT"),
        "alg" -> JsString("HS256")
      ))
    }
  }

}
