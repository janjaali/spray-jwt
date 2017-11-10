package org.janjaali.sprayjwt

import java.security.Security

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.janjaali.sprayjwt.algorithms.HashingAlgorithm
import org.janjaali.sprayjwt.encoder.{Base64Decoder, Base64Encoder}
import org.janjaali.sprayjwt.exceptions.{InvalidJwtException, InvalidSignatureException}
import org.janjaali.sprayjwt.headers.{JwtHeader, JwtHeaderJsonWriter}
import spray.json._

import scala.util.{Success, Try}

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
  def encode(payload: String, secret: String, algorithm: HashingAlgorithm): Try[String] = {
    Try(encode(payload.parseJson, secret, algorithm, None))
  }

  def encode(payload: String, secret: String, algorithm: HashingAlgorithm, jwtClaims: JwtClaims): Try[String] = {
    Try(encode(payload.parseJson, secret, algorithm, Some(jwtClaims)))
  }

  /**
    * Encodes payload as JWT.
    *
    * @param payload   to encode
    * @param secret    secret to use
    * @param algorithm algorithm to use for encoding
    * @return encoded JWT
    */
  def encode(payload: JsValue, secret: String, algorithm: HashingAlgorithm): Try[String] = {
    Try(encode(payload, secret, algorithm, None))
  }

  def encode(payload: JsValue, secret: String, algorithm: HashingAlgorithm, jwtClaims: JwtClaims): Try[String] = {
    Try(encode(payload, secret, algorithm, Some(jwtClaims)))
  }

  /**
    * Decodes JWT token as String.
    *
    * @param token  to decode
    * @param secret to use for decoding
    * @return String decoded JWT token
    */
  def decodeAsString(token: String, secret: String): Try[String] = {
    val splitToken = token.split("\\.")
    if (splitToken.length != 3) {
      throw new InvalidJwtException("JWT must have form header.payload.signature")
    }

    val header = splitToken(0)
    val payload = splitToken(1)
    val data = s"$header.$payload"

    val signature = splitToken(2)

    val algorithm = getAlgorithmFromHeader(header)

    if (!algorithm.validate(data, signature, secret)) {
      throw new InvalidSignatureException()
    }

    val payloadDecoded = Base64Decoder.decodeAsString(payload)
    Success(payloadDecoded)
  }

  /**
    * Decodes JWT token as JsValue.
    *
    * @param token  to decode
    * @param secret to use for decoding
    * @return JsValue decoded JWT token
    */
  def decode(token: String, secret: String): Try[JsValue] = {
    decodeAsString(token, secret).map(_.parseJson)
  }

  private def getAlgorithmFromHeader(header: String): HashingAlgorithm = {
    val headerDecoded = Base64Decoder.decodeAsString(header)
    val jwtHeader = headerDecoded.parseJson.convertTo[JwtHeader]
    jwtHeader.algorithm
  }

  private def encode(payload: JsValue, secret: String, algorithm: HashingAlgorithm, jwtClaims: Option[JwtClaims]): String = {
    val fields = payload.asJsObject.fields
    val reversedClaims = jwtClaims.map(getReversedClaims).getOrElse(Map.empty)

    val payloadWithReservedClaims = JsObject(fields ++ reversedClaims)

    val encodedHeader = getEncodedHeader(algorithm)
    val encodedPayload = Base64Encoder.encode(payloadWithReservedClaims.toString)

    val encodedData = s"$encodedHeader.$encodedPayload"

    val signature = algorithm.sign(encodedData, secret)
    s"$encodedData.$signature"
  }

  private def addBouncyCastleProvider(): Unit = {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider)
    }
  }

  private def getEncodedHeader(algorithm: HashingAlgorithm): String = {
    val header = JwtHeader(algorithm).toJson.toString
    Base64Encoder.encode(header)
  }

  private def getReversedClaims(jwtClaims: JwtClaims): Map[String, JsValue] = {
    Seq(
      "iss" -> jwtClaims.iss,
      "sub" -> jwtClaims.sub,
      "aud" -> jwtClaims.aud,
      "exp" -> jwtClaims.exp,
      "nbf" -> jwtClaims.nbf,
      "isa" -> jwtClaims.isa,
      "iat" -> jwtClaims.iat,
      "jti" -> jwtClaims.jti
    ).filter(_._2.nonEmpty)
      .map {
        case (name, Some(value: String)) => name -> JsString(value)
        case (name, Some(values: Set[String])) => name -> (
          if (values.size == 1) JsString(values.head) else JsArray(values.map(JsString(_)).toVector)
        )
        case (name, Some(value: Long)) => name -> JsNumber(value)
      }
      .toMap
  }

}
