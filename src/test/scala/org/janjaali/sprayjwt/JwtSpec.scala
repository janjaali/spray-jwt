package org.janjaali.sprayjwt

import org.janjaali.sprayjwt.algorithms.{HS256, HS384, HS512}
import org.scalatest.FunSpec

class JwtSpec extends FunSpec {

  describe("Jwt encodes the header to JSON") {
    describe("HS256") {
      it("encodes as JWT") {
        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = Jwt.encode(payload, "secret", HS256)

        val expectedJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
        assert(jwt == expectedJwt)
      }
    }

    describe("HS384") {
      it("encodes as JWT") {
        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = Jwt.encode(payload, "secret", HS384)

        val expectedJwt = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.DtVnCyiYCsCbg8gUP+579IC2GJ7P3CtFw6nfTTPw+0lZUzqgWAo9QIQElyxOpoRm"
        assert(jwt == expectedJwt)
      }
    }

    describe("HS512") {
      it("encodes as JWT") {
        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = Jwt.encode(payload, "secret", HS512)

        val expectedJwt = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.YI0rUGDq5XdRw8vW2sDLRNFMN8Waol03iSFH8I4iLzuYK7FKHaQYWzPt0BJFGrAmKJ6SjY0mJIMZqNQJFVpkuw"
        assert(jwt == expectedJwt)
      }
    }
  }

}
