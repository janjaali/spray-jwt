package org.janjaali.sprayjwt.algorithms

import org.janjaali.sprayjwt.encoder.Base64UrlEncoder
import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits._
import org.janjaali.sprayjwt.json.{JsonStringSerializer, JsonValue}
import org.janjaali.sprayjwt.jws.{Header, JoseHeader, JwsPayload, JwsSignature}
import org.janjaali.sprayjwt.jwt.{Claim, JwtClaimsSet, NumericDate}
import org.janjaali.sprayjwt.tests.ScalaTestSpec

trait JsonStringSerializerSpec extends ScalaTestSpec {

  private implicit val base64UrlEncoder: Base64UrlEncoder = Base64UrlEncoder

  protected def jsonStringSerializer: JsonStringSerializer

  protected def verifySignWithHmac256Algorithm(): Unit = {

    verifySignWithAlgorithm(
      algorithm = Algorithm.Hmac.Hs256,
      expectedSignature = JwsSignature(
        "jUzTJEnlFeTXDUPp9vJMwoalvXJ55IZ6DaBExN08UtA"
      )
    )
  }

  protected def verifySignWithHmac384Algorithm(): Unit = {

    verifySignWithAlgorithm(
      algorithm = Algorithm.Hmac.Hs384,
      expectedSignature = JwsSignature(
        "tz6NV8IfhPNqEnfUgeu0TJowwvWsjcmFCiRC_F-7bTOQeUle8jomj151nYHx1-IQ"
      )
    )
  }

  protected def verifySignWithHmac512Algorithm(): Unit = {

    verifySignWithAlgorithm(
      algorithm = Algorithm.Hmac.Hs512,
      expectedSignature = JwsSignature(
        "dOf7rSkv-y62jQDwAuzNdNKX2jfYK2HREBYqlB0rLnlERIlWkQ4BkVbbVyGi47br1Os4FllE4yjuz_FVjabK5w"
      )
    )
  }

  private def verifySignWithAlgorithm(
      algorithm: Algorithm,
      expectedSignature: JwsSignature
  ): Unit = {

    s"Verify serializer with algorithm '${algorithm}'." in {

      val joseHeader = JoseHeader(
        Seq(
          Header.Type(Header.Type.Value.Jwt),
          // TODO: That's weird, how can we set the algorithm and then just
          //  sign with another one?
          Header.Algorithm(algorithm)
        )
      )

      val jwsPayload = JwsPayload(
        JwtClaimsSet(
          Seq(
            // TODO: Maybe add an iss type?
            Claim.Private(name = "iss", value = "joe"),
            Claim.ExpirationTime(NumericDate(1300819380L)),
            Claim.Private(name = "http://example.com/is_root", value = true)
          )
        )
      )

      val secret = Secret("secret value")

      algorithm.sign(joseHeader, jwsPayload, secret) shouldBe expectedSignature
    }
  }

  private implicit def serializeJson: JsonValue => String = {
    jsonStringSerializer.serialize
  }
}
