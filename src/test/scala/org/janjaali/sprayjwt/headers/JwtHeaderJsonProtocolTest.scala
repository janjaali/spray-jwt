package org.janjaali.sprayjwt.headers

import org.janjaali.sprayjwt.algorithms.HS256
import org.scalatest.FunSpec
import spray.json._

class JwtHeaderJsonProtocolTest extends FunSpec with JwtHeaderJsonProtocol {

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
