package org.janjaali.sprayjwt.algorithms

import org.janjaali.sprayjwt.encoder.{Base64UrlDecoder, Base64UrlEncoder}
import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits._
import org.janjaali.sprayjwt.json.{
  JsonStringDeserializer,
  JsonStringSerializer,
  JsonValue
}
import org.janjaali.sprayjwt.jws.{Header, JoseHeader, JwsPayload, JwsSignature}
import org.janjaali.sprayjwt.jwt.{Claim, JwtClaimsSet, NumericDate}
import org.janjaali.sprayjwt.tests.ScalaTestSpec

trait JsonSupportSpec extends ScalaTestSpec:

  protected given jsonStringSerializer: JsonStringSerializer

  protected given jsonStringDeserializer: JsonStringDeserializer

  protected def verifySignWithHmacAlgorithms(): Unit =
    verifySignWithHmac256Algorithm()
    verifySignWithHmac384Algorithm()
    verifySignWithHmac512Algorithm()

  protected def verifyValidationWithHmacAlgorithms(): Unit =
    verifyValidationWithHmac256Algorithm()
    verifyValidationWithHmac384Algorithm()
    verifyValidationWithHmac512Algorithm()

  private def verifySignWithHmac256Algorithm(): Unit =
    verifySignWithAlgorithm(
      algorithm = Algorithms.Hs256,
      expectedSignature = JwsSignature(
        "jUzTJEnlFeTXDUPp9vJMwoalvXJ55IZ6DaBExN08UtA"
      )
    )

  private def verifySignWithHmac384Algorithm(): Unit =
    verifySignWithAlgorithm(
      algorithm = Algorithms.Hs384,
      expectedSignature = JwsSignature(
        "tz6NV8IfhPNqEnfUgeu0TJowwvWsjcmFCiRC_F-7bTOQeUle8jomj151nYHx1-IQ"
      )
    )

  private def verifySignWithHmac512Algorithm(): Unit =
    verifySignWithAlgorithm(
      algorithm = Algorithms.Hs512,
      expectedSignature = JwsSignature(
        "dOf7rSkv-y62jQDwAuzNdNKX2jfYK2HREBYqlB0rLnlERIlWkQ4BkVbbVyGi47br1Os4FllE4yjuz_FVjabK5w"
      )
    )

  private def verifySignWithAlgorithm(
      algorithm: Algorithm,
      expectedSignature: JwsSignature
  ): Unit =
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

  private def verifyValidationWithHmac256Algorithm(): Unit =
    verifyValidationWithHmacAlgorithm(
      algorithmName = "Hmac256",
      data = {
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.eMS0OOcs-0Eo5x5vDepYOcITeG4NtPrtE8seTsT1RT0"
      },
      secret = Secret("secret value")
    )

  private def verifyValidationWithHmac384Algorithm(): Unit =
    verifyValidationWithHmacAlgorithm(
      algorithmName = "Hmac384",
      data = {
        "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.tXvOFupOdp6ISki7ysqn_gO9LHk2n35d0f_E26d9FYfhLLHNudoWU2HXD_6Tnm7X"
      },
      secret = Secret("secret value")
    )

  private def verifyValidationWithHmac512Algorithm(): Unit =
    verifyValidationWithHmacAlgorithm(
      algorithmName = "Hmac512",
      data = {
        "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.mV13yTaWA-6cQWcQEmUOh2qaTnpF5hQpNNkIMY6RIlbyQQQ-MbE9zjQ19dTQpQ2hxEql5ObbmDmWzqx13ka6Iw",
      },
      secret = Secret("secret value")
    )

  private def verifyValidationWithHmacAlgorithm(
      algorithmName: String,
      data: String,
      secret: Secret
  ): Unit =
    s"Verify deserializer with '$algorithmName'." in {
      Algorithms.validate(data, secret) shouldBe true
    }
