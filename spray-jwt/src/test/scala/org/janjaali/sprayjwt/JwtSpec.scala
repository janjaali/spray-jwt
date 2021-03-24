package org.janjaali.sprayjwt

import org.janjaali.sprayjwt.algorithms._
import org.scalatest.funspec.AnyFunSpec
import spray.json.{JsBoolean, JsObject, JsString}

class JwtSpec extends AnyFunSpec {

  describe("Jwt encodes the header to JSON") {
    describe("HS256") {
      val secret = "secret"

      it("encodes as JWT") {
        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = LegacyJwt.encode(payload, secret, Algorithm.Hmac.Hs256).get

        // scalastyle:off
        val expectedJwt =
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("encodes as JWT with iss") {
        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = LegacyJwt
          .encode(
            payload,
            secret,
            Algorithm.Hmac.Hs256,
            JwtClaims(iss = Some("issuer"))
          )
          .get

        // scalastyle:off
        val expectedJwt =
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlzcyI6Imlzc3VlciJ9.MabLKH7FNuQXlshZe6m054If8TLP5DvwYccl0ejlUVA"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("encodes JsValue as JWT") {
        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jsValue = JsObject(
          "sub" -> JsString("1234567890"),
          "name" -> JsString("John Doe"),
          "admin" -> JsBoolean(true)
        )

        val jwt = LegacyJwt.encode(jsValue, secret, Algorithm.Hmac.Hs256).get

        // scalastyle:off
        val expectedJwt =
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("encodes JsValue as JWT with iss") {
        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jsValue = JsObject(
          "sub" -> JsString("1234567890"),
          "name" -> JsString("John Doe"),
          "admin" -> JsBoolean(true)
        )

        val jwt = LegacyJwt
          .encode(
            jsValue,
            secret,
            Algorithm.Hmac.Hs256,
            JwtClaims(iss = Some("issuer"))
          )
          .get

        // scalastyle:off
        val expectedJwt =
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlzcyI6Imlzc3VlciJ9.MabLKH7FNuQXlshZe6m054If8TLP5DvwYccl0ejlUVA"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("encodes as JWT with all reserved claims") {
        // scalastyle:off
        val payload =
          """{"sub":"1234567890","name":"John Doe","admin":true}"""

        val jwt = LegacyJwt
          .encode(
            payload,
            secret,
            Algorithm.Hmac.Hs256,
            JwtClaims(
              iss = Some("issuer"),
              sub = Some("subject"),
              aud = Some(Set("audience")),
              isa = Some(500),
              exp = Some(1000),
              nbf = Some(2000),
              iat = Some(3000),
              jti = Some("jwtId")
            )
          )
          .get

        val expectedJwt =
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjIwMDAsImFkbWluIjp0cnVlLCJuYW1lIjoiSm9obiBEb2UiLCJqdGkiOiJqd3RJZCIsImV4cCI6MTAwMCwiaXNhIjo1MDAsImlhdCI6MzAwMCwic3ViIjoic3ViamVjdCIsImF1ZCI6ImF1ZGllbmNlIiwiaXNzIjoiaXNzdWVyIn0.2zS7vqKCLPKOlre6LYMMR/dTp41Q9jV5KiEyE9I6JLw"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("decodes JWT as String") {
        // scalastyle:off
        val token =
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
        // scalastyle:on

        val decodedPayload = LegacyJwt.decodeAsString(token, secret).get
        val expected = """{"sub":"1234567890","name":"John Doe","admin":true}"""

        assert(decodedPayload == expected)
      }

      it("decodes JWT as JsValue") {
        // scalastyle:off
        val token =
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
        // scalastyle:on

        val json = LegacyJwt.decode(token, secret).get

        val expected = JsObject(
          "sub" -> JsString("1234567890"),
          "name" -> JsString("John Doe"),
          "admin" -> JsBoolean(true)
        )
        assert(json == expected)
      }

      it("decodes JWT as JsValue with iss claim") {
        // scalastyle:off
        val token =
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlzcyI6Imlzc3VlciJ9.MabLKH7FNuQXlshZe6m054If8TLP5DvwYccl0ejlUVA"
        // scalastyle:on

        val json = LegacyJwt.decode(token, secret).get

        val expected = JsObject(
          "sub" -> JsString("1234567890"),
          "name" -> JsString("John Doe"),
          "admin" -> JsBoolean(true),
          "iss" -> JsString("issuer")
        )
        assert(json == expected)
      }
    }

    describe("HS384") {
      val secret = "secret"

      it("encodes as JWT") {
        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = LegacyJwt.encode(payload, secret, Algorithm.Hmac.Hs384).get

        // scalastyle:off
        val expectedJwt =
          "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.DtVnCyiYCsCbg8gUP+579IC2GJ7P3CtFw6nfTTPw+0lZUzqgWAo9QIQElyxOpoRm"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("decodes JWT as String") {
        // scalastyle:off
        val token =
          "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.DtVnCyiYCsCbg8gUP+579IC2GJ7P3CtFw6nfTTPw+0lZUzqgWAo9QIQElyxOpoRm"
        // scalastyle:on

        val decodedPayload = LegacyJwt.decodeAsString(token, secret).get

        val expected = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        assert(decodedPayload == expected)
      }
    }

    describe("HS512") {
      val secret = "secret"

      it("encodes as JWT") {
        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = LegacyJwt.encode(payload, secret, Algorithm.Hmac.Hs512).get

        // scalastyle:off
        val expectedJwt =
          "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.YI0rUGDq5XdRw8vW2sDLRNFMN8Waol03iSFH8I4iLzuYK7FKHaQYWzPt0BJFGrAmKJ6SjY0mJIMZqNQJFVpkuw"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("decodes JWT as String") {
        // scalastyle:off
        val token =
          "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.DtVnCyiYCsCbg8gUP+579IC2GJ7P3CtFw6nfTTPw+0lZUzqgWAo9QIQElyxOpoRm"
        // scalastyle:on

        val decodedPayload = LegacyJwt.decodeAsString(token, secret).get

        val expected = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        assert(decodedPayload == expected)
      }
    }

    describe("RS256") {
      it("encodes as JWT") {
        val source =
          scala.io.Source.fromURL(getClass.getResource("test.rsa.private.key"))
        val secret =
          try source.mkString
          finally source.close

        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = LegacyJwt.encode(payload, secret, Algorithm.Rsa.Rs256).get

        // scalastyle:off
        val expectedJwt =
          "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.CqkpMs2+ttnOcZMjCk3ca2Fw0d3yGBsS5X+eEtuPhYV77ApgRAidZqZvsC1Cs5hqhX6ZTuer0UnCAQ5n4gvyLoaMiMiGqtm+UeHiUKQSeThtqf4M5ylMERi971gZV5ffXPeAHUZUPN8IiMof2BjUwOk4cN7WVfz5i80zcXAkbBUcra2uPlvVpHXGrIVI3CPpBYs4Hn3towNHX9bpWnqfvogy5TXzMEVHAF8H/TgGDwmCMuIGmi4xdlVviXTXrF/znPNNowTuI8aaXenJRYaDkI0VyN6MChmsA8aDOMMSlikDrgGzdxQSGJrBSrvrjnuJMK9raJ7dr/1U+5Rghtms+Q"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("decodes JWT as String") {
        val source =
          scala.io.Source.fromURL(getClass.getResource("test.rsa.public.key"))
        val public =
          try source.mkString
          finally source.close

        // scalastyle:off
        val token =
          "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.CqkpMs2+ttnOcZMjCk3ca2Fw0d3yGBsS5X+eEtuPhYV77ApgRAidZqZvsC1Cs5hqhX6ZTuer0UnCAQ5n4gvyLoaMiMiGqtm+UeHiUKQSeThtqf4M5ylMERi971gZV5ffXPeAHUZUPN8IiMof2BjUwOk4cN7WVfz5i80zcXAkbBUcra2uPlvVpHXGrIVI3CPpBYs4Hn3towNHX9bpWnqfvogy5TXzMEVHAF8H/TgGDwmCMuIGmi4xdlVviXTXrF/znPNNowTuI8aaXenJRYaDkI0VyN6MChmsA8aDOMMSlikDrgGzdxQSGJrBSrvrjnuJMK9raJ7dr/1U+5Rghtms+Q"
        // scalastyle:on

        val decoded = LegacyJwt.decodeAsString(token, public).get

        val expected = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        assert(decoded == expected)
      }
    }

    describe("RS384") {
      it("encodes as JWT") {
        val source =
          scala.io.Source.fromURL(getClass.getResource("test.rsa.private.key"))
        val secret =
          try source.mkString
          finally source.close

        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = LegacyJwt.encode(payload, secret, Algorithm.Rsa.Rs384).get

        // scalastyle:off
        val expectedJwt =
          "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.oCcq/xI4jcjxMB5zRSp6F7bfQjT2KhdH4fJkN3E24wa6ltE2UufgXQ4/wJjOalJ4h0RbnEUwlMzdD3hJgNRU7BfD6r5GzVo/RLTLTkyTD+KsHXYiS4qHYOZ1otyoPFV/QzQcovoOXT+kmsVH/S6mpVzN1Qh1OUgu+2D9swH+6rZi0YctrKv3dXou+GSVt1l5xfyA7R4KB8HwONTwdyEbTSM/aJWP+Ob80kDNAEs9xkx/2KzY1iGfdh0FwIU2OKdc+b0CNlVQrbYwLX55Yk5CBPJY6UNlXwSmCFwlbZvjChkwE5MH4ICDLWN7j2llm6PX38RE2xRCguqId/iA8vwj8g"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("decodes JWT as String") {
        val source =
          scala.io.Source.fromURL(getClass.getResource("test.rsa.public.key"))
        val public =
          try source.mkString
          finally source.close

        // scalastyle:off
        val token =
          "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.oCcq/xI4jcjxMB5zRSp6F7bfQjT2KhdH4fJkN3E24wa6ltE2UufgXQ4/wJjOalJ4h0RbnEUwlMzdD3hJgNRU7BfD6r5GzVo/RLTLTkyTD+KsHXYiS4qHYOZ1otyoPFV/QzQcovoOXT+kmsVH/S6mpVzN1Qh1OUgu+2D9swH+6rZi0YctrKv3dXou+GSVt1l5xfyA7R4KB8HwONTwdyEbTSM/aJWP+Ob80kDNAEs9xkx/2KzY1iGfdh0FwIU2OKdc+b0CNlVQrbYwLX55Yk5CBPJY6UNlXwSmCFwlbZvjChkwE5MH4ICDLWN7j2llm6PX38RE2xRCguqId/iA8vwj8g"
        // scalastyle:on

        val decoded = LegacyJwt.decodeAsString(token, public).get

        val expected = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        assert(decoded == expected)
      }
    }

    describe("RS512") {
      it("encodes as JWT") {
        val source =
          scala.io.Source.fromURL(getClass.getResource("test.rsa.private.key"))
        val secret =
          try source.mkString
          finally source.close

        val payload = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        val jwt = LegacyJwt.encode(payload, secret, Algorithm.Rsa.Rs512).get

        // scalastyle:off
        val expectedJwt =
          "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.JuhmKgWohwJS9lLhBo5ealB+RA5CX6VgzYExvUKKL/v5oqEO1bZ91Ayi949jvB5tTgvMOX3njfKpl8tyazfqoHjsXzRvHX/NdUGx1rWhWZ826Zdpgm32fO15Jv1xHbxWbFaqp0zwyLUKPo756lmg1+8IeTBdDvhC7XSlBc9cUDe4x3anltjeUseZllS2PZgQn0pxYXK5KVbAsIasDthprmaJheLBgO+CInCpDiVukUC2WfCGz9tr9IhKwNgLPkcue4uVRubOgV8By68SMZgVdxZXP70siV/sMOqrILyWk7Zi0fSm/JC4QP4fZSenfxwl8FEr4Rs+FWL/clk3fnMYQA"
        // scalastyle:on

        assert(jwt == expectedJwt)
      }

      it("decodes JWT as String") {
        val source =
          scala.io.Source.fromURL(getClass.getResource("test.rsa.public.key"))
        val public =
          try source.mkString
          finally source.close

        // scalastyle:off
        val token =
          "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.JuhmKgWohwJS9lLhBo5ealB+RA5CX6VgzYExvUKKL/v5oqEO1bZ91Ayi949jvB5tTgvMOX3njfKpl8tyazfqoHjsXzRvHX/NdUGx1rWhWZ826Zdpgm32fO15Jv1xHbxWbFaqp0zwyLUKPo756lmg1+8IeTBdDvhC7XSlBc9cUDe4x3anltjeUseZllS2PZgQn0pxYXK5KVbAsIasDthprmaJheLBgO+CInCpDiVukUC2WfCGz9tr9IhKwNgLPkcue4uVRubOgV8By68SMZgVdxZXP70siV/sMOqrILyWk7Zi0fSm/JC4QP4fZSenfxwl8FEr4Rs+FWL/clk3fnMYQA"
        // scalastyle:on

        val decoded = LegacyJwt.decodeAsString(token, public).get

        val expected = """{"sub":"1234567890","name":"John Doe","admin":true}"""
        assert(decoded == expected)
      }
    }
  }

}
