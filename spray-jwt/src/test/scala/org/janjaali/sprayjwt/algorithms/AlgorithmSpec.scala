package org.janjaali.sprayjwt.algorithms

import org.janjaali.sprayjwt.encoder.Base64Encoder
import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits._
import org.janjaali.sprayjwt.json.{JsonStringSerializer, JsonValue}
import org.janjaali.sprayjwt.jws.{Header, JoseHeader, JwsPayload}
import org.janjaali.sprayjwt.jwt.{Claim, JwtClaimsSet, NumericDate}
import org.janjaali.sprayjwt.tests.ScalaTestSpec
import org.janjaali.sprayjwt.jws.JwsSignature

trait AlgorithmSpec extends ScalaTestSpec {

  implicit val base64UrlEncoder: Base64Encoder = Base64Encoder

  protected def signWithHmacAlgorithm(
      algorithm: Algorithm
  )(implicit serializeJson: JsonValue => String) = {

    s"Algorithm '${algorithm}'" - {

      "should sign JOSE Header and JWS Payload with a specified secret." in {

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

        algorithm.sign(joseHeader, jwsPayload, secret) shouldBe JwsSignature(
          "dance"
        )
      }
    }
  }
}
