package org.janjaali.sprayjwt

import org.janjaali.sprayjwt.algorithms.HS256
import org.scalatest.FunSpec

class JwtSpec extends FunSpec {

  describe("Jwt encodes the header to JSON") {
    it("encodes as JWT") {
      val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
      val jwt = Jwt.encode(payload, "secret", HS256)

      val expectedJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
      assert(jwt == expectedJwt)
    }
  }

}
