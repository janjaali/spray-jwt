package org.janjaali.sprayjwt.encoder

import org.janjaali.sprayjwt.tests.ScalaTestSpec

final class Base64UrlUrlEncoderSpec extends ScalaTestSpec {

  "Base64UrlEncoder" - {

    val sut = Base64UrlEncoder

    "encodes text as Base64 URL encoded String." in {

      val text = "{\"typ\":\"JWT\",\r\n \"alg\":\"HS256\"}"

      sut.encode(text) shouldBe "eyJ0eXAiOiJKV1QiLA0KICJhbGciOiJIUzI1NiJ9"
    }

    "encodes text as Base64 URL encoded String without padding." in {

      val text = {
        "{\"iss\":\"joe\",\r\n \"exp\":1300819380,\r\n \"http://example.com/is_root\":true}"
      }

      sut.encode(text) shouldBe {
        "eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly" +
        "9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ"
      }
    }

    "encoded byte array as Base64 URL encoded String." in {

      val text = "{\"typ\":\"JWT\",\r\n \"alg\":\"HS256\"}".getBytes("UTF-8")

      sut.encode(text) shouldBe "eyJ0eXAiOiJKV1QiLA0KICJhbGciOiJIUzI1NiJ9"
    }
  }
}
