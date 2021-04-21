package org.janjaali.sprayjwt.algorithms

import org.janjaali.sprayjwt.encoder.Base64UrlEncoder
import org.janjaali.sprayjwt.tests.ScalaTestSpec

final class AlgorithmSpec extends ScalaTestSpec {

  private implicit val base64UrlEncoder = Base64UrlEncoder

  "Algorithm" - {

    "should validate raw JWT string representation" - {

      "when signed with" - {

        "HS256." in {

          Algorithm.Hmac.Hs256.validate(
            data = {
              "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJqb2UiLCJleHAiOjEzMDA4MTkzODAsImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ.dOf7rSkv-y62jQDwAuzNdNKX2jfYK2HREBYqlB0rLnlERIlWkQ4BkVbbVyGi47br1Os4FllE4yjuz_FVjabK5w",
            },
            secret = Secret("secret value")
          ) shouldBe false
        }

        "Hs512." in {

          Algorithm.Hmac.Hs512.validate(
            data = {
              "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJqb2UiLCJleHAiOjEzMDA4MTkzODAsImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ.dOf7rSkv-y62jQDwAuzNdNKX2jfYK2HREBYqlB0rLnlERIlWkQ4BkVbbVyGi47br1Os4FllE4yjuz_FVjabK5w",
            },
            secret = Secret("secret value")
          ) shouldBe false
        }
      }
    }
  }
}
