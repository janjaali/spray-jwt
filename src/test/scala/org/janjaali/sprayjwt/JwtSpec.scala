package org.janjaali.sprayjwt

import org.janjaali.sprayjwt.algorithms.HS256
import org.scalatest.FunSpec

class JwtSpec extends FunSpec {

  describe("Jwt encodes the header to JSON") {
    it("encodes") {
      assert(Jwt.encode("some", "secret", HS256) == """{"alg": "HS256", "typ": "JWT"}""")
    }
  }

}
