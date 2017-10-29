package org.janjaali.sprayjwt

import java.security.Security

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.janjaali.sprayjwt.algorithms.HashingAlgorithm
import org.janjaali.sprayjwt.encoder.Base64Encoder
import org.janjaali.sprayjwt.headers.{JwtHeader, JwtHeaderJsonWriter}
import spray.json._

/**
  * Represents a JWT Encoder/Decoder.
  */
object Jwt {

  addBouncyCastleProvider()

  /**
    * Encodes payload as JWT.
    *
    * @param payload   to encode
    * @param secret    secret to use
    * @param algorithm algorithm to use for encoding
    * @return encoded JWT
    */
  def encode(payload: String, secret: String, algorithm: HashingAlgorithm): String = {
    encode(JwtHeader(algorithm), payload, algorithm, secret)
  }

  /**
    * Encodes payload as JWT.
    *
    * @param payload   to encode
    * @param secret    secret to use
    * @param algorithm algorithm to use for encoding
    * @return encoded JWT
    */
  def encode(payload: JsValue, secret: String, algorithm: HashingAlgorithm): String = {
    encode(payload.toString, secret, algorithm)
  }

  private def encode(header: JwtHeader, payload: String, algorithm: HashingAlgorithm, secret: String): String = {
    val encodedHeader = Base64Encoder.encode(header.toJson.toString)
    val encodedPayload = Base64Encoder.encode(payload)

    val encodedData = s"$encodedHeader.$encodedPayload"

    val signature = algorithm.sign(encodedData, secret)
    s"$encodedData.$signature"
  }

  private def addBouncyCastleProvider(): Unit = {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider)
    }
  }

}
