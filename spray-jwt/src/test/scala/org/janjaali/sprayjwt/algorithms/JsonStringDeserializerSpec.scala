package org.janjaali.sprayjwt.algorithms

import org.janjaali.sprayjwt.encoder.{Base64UrlDecoder, Base64UrlEncoder}
import org.janjaali.sprayjwt.json.{JsonStringDeserializer, JsonValue}
import org.janjaali.sprayjwt.tests.ScalaTestSpec

trait JsonStringDeserializerSpec extends ScalaTestSpec {

  private implicit val base64UrlEncoder: Base64UrlEncoder = Base64UrlEncoder

  private implicit val base64UrlDecoder: Base64UrlDecoder = Base64UrlDecoder

  protected def jsonStringDeserializer: JsonStringDeserializer

  protected def verifyValidationWithHmacAlgorithms(): Unit = {

    verifyValidationWithHmac256Algorithm()

    verifyValidationWithHmac384Algorithm()

    verifyValidationWithHmac512Algorithm()
  }

  private def verifyValidationWithHmac256Algorithm(): Unit = {

    verifyValidationWithHmacAlgorithm(
      algorithmName = "Hmac256",
      data = {
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.eMS0OOcs-0Eo5x5vDepYOcITeG4NtPrtE8seTsT1RT0"
      },
      secret = Secret("secret value")
    )
  }

  private def verifyValidationWithHmac384Algorithm(): Unit = {

    verifyValidationWithHmacAlgorithm(
      algorithmName = "Hmac384",
      data = {
        "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.tXvOFupOdp6ISki7ysqn_gO9LHk2n35d0f_E26d9FYfhLLHNudoWU2HXD_6Tnm7X"
      },
      secret = Secret("secret value")
    )
  }

  private def verifyValidationWithHmac512Algorithm(): Unit = {

    verifyValidationWithHmacAlgorithm(
      algorithmName = "Hmac512",
      data = {
        "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.mV13yTaWA-6cQWcQEmUOh2qaTnpF5hQpNNkIMY6RIlbyQQQ-MbE9zjQ19dTQpQ2hxEql5ObbmDmWzqx13ka6Iw",
      },
      secret = Secret("secret value")
    )
  }

  private def verifyValidationWithHmacAlgorithm(
      algorithmName: String,
      data: String,
      secret: Secret
  ): Unit = {

    s"Verify deserializer with '$algorithmName'." in {

      Algorithm.validate(data, secret) shouldBe true
    }
  }

  private implicit def serializeJson: String => JsonValue = {
    jsonStringDeserializer.deserialize
  }
}
