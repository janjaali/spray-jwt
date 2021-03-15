package org.janjaali.sprayjwt

import java.security.Security

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.janjaali.sprayjwt.algorithms.Algorithm
import org.janjaali.sprayjwt.encoder.{Base64Decoder, Base64Encoder}
import org.janjaali.sprayjwt.exceptions.{
  InvalidJwtException,
  InvalidSignatureException
}
import org.janjaali.sprayjwt.headers.{JwtHeader, JwtHeaderJsonWriter}
import spray.json._

import scala.util.{Success, Try}

import org.janjaali.sprayjwt.json._
import org.janjaali.sprayjwt.jwt.model.ClaimsSet
import org.janjaali.sprayjwt.jwt.model.JwsPayload

/** TODO:
  */
trait Claim {

  type T

  def key: String

  def value: T

  def asJson: JsonValue
}

sealed trait Secret

/** Represents a JWT consisting of the encoded parts:
  *
  * <ol>
  *   <li>JOSE Header</li>
  *   <li>JWS Payload</li>
  *   <li>JWS Signature</li>
  * </ol>
  *
  * @param jose
  * @param claims
  * @param signature
  */
sealed abstract case class Jwt private (
    joseHeader: String,
    jwsPayload: String,
    jwsSignature: String
) {

  /** Raw encoded string representation of this JWT.
    *
    * @return raw JWT string
    */
  def raw: String = {
    s"$joseHeader.$jwsPayload.$jwsSignature"
  }
}

/** Represents a JOSE Header containing the parameters describing the
  * cryptographic operations and parameters employed to encode a JWT value.
  *
  *   // TODO: Is this the JWS Protected Header? (JWS Header)
  *
  * @param algorithm TODO: define
  */
private case class JoseHeader(algorithm: Algorithm) {

  private val typ = "JWT"

  /** JSON representation of this JOSE Header.
    *
    * @return JSON object
    */
  def asJson: JsonObject = {
    JsonObject(
      Map(
        "typ" -> JsonString(this.typ),
        "alg" -> JsonString(algorithm.name)
      )
    )
  }
}

object Jwt {

  def apply(
      claims: ClaimsSet,
      algorithm: Algorithm,
      secret: String
  )(implicit jsonStringSerializer: JsonStringSerializer): Jwt = {

    Jwt(
      joseHeader = JoseHeader(algorithm),
      jwsPayload = JwsPayload(claims)
    )
  }

  private def apply(
      joseHeader: JoseHeader,
      jwsPayload: JwsPayload
  )(implicit jsonStringSerializer: JsonStringSerializer): Jwt = {

    val joseHeaderJson = joseHeader.asJson
    val joseHeaderJsonBase64Encoded = Base64Encoder.encode(joseHeaderJson)

    val jwsPayloadJson = jwsPayload.asJson
    val jwsPayloadJsonBase64Encoded = Base64Encoder.encode(jwsPayloadJson)

    new Jwt(
      joseHeader = joseHeaderJsonBase64Encoded,
      jwsPayload = jwsPayloadJsonBase64Encoded,
      jwsSignature = ???
    ) {}
  }
}

/** JWT Encoder/Decoder.
  */
object LegacyJwt {

  addBouncyCastleProvider()

  /** Encodes payload as JWT.
    *
    * @param payload   the payload to encode as JWT's payload
    * @param secret    the secret which is used to sign JWT
    * @param algorithm the hashing algorithm used for encoding
    * @return encoded JWT
    */
  def encode(
      payload: String,
      secret: String,
      algorithm: Algorithm
  ): Try[String] = {
    Try(encode(payload.parseJson, secret, algorithm, None))
  }

  /** Encodes payload as JWT.
    *
    * @param payload   the payload to encode as JWT's payload
    * @param secret    the secret which is used to sign JWT
    * @param algorithm the hashing algorithm used for encoding
    * @param jwtClaims reserved JWT claims to add to payload (specified claims will overwrite equal named claims in
    *                  payload)
    * @return encoded JWT
    */
  def encode(
      payload: String,
      secret: String,
      algorithm: Algorithm,
      jwtClaims: JwtClaims
  ): Try[String] = {
    Try(encode(payload.parseJson, secret, algorithm, Some(jwtClaims)))
  }

  /** Encodes payload as JWT.
    *
    * @param payload   the payload to encode as JWT's payload
    * @param secret    the secret which is used to sign JWT
    * @param algorithm the hashing algorithm used for encoding
    * @return encoded JWT
    */
  def encode(
      payload: JsValue,
      secret: String,
      algorithm: Algorithm
  ): Try[String] = {
    Try(encode(payload, secret, algorithm, None))
  }

  /** Encodes payload as JWT.
    *
    * @param payload   the payload to encode as JWT's payload
    * @param secret    the secret which is used to sign JWT
    * @param algorithm the hashing algorithm used for encoding
    * @param jwtClaims reserved JWT claims to add to payload (specified claims will overwrite equal named claims in
    *                  payload)
    * @return encoded JWT
    */
  def encode(
      payload: JsValue,
      secret: String,
      algorithm: Algorithm,
      jwtClaims: JwtClaims
  ): Try[String] = {
    Try(encode(payload, secret, algorithm, Some(jwtClaims)))
  }

  /** Decodes JWT token as JsValue.
    *
    * @param token  the JWT token to decode
    * @param secret the secret to use to validate signature of JWT
    * @return JsValue decoded JWT
    */
  def decode(token: String, secret: String): Try[JsValue] = {
    decodeAsString(token, secret).map(_.parseJson)
  }

  /** Decodes JWT token as JsValue.
    *
    * @param token  the JWT token to decode
    * @param secret the secret to use to validate signature of JWT
    * @return JsValue decoded JWT
    */
  def decodeAsString(token: String, secret: String): Try[String] = {
    val splitToken = token.split("\\.")
    if (splitToken.length != 3) {
      throw new InvalidJwtException(
        "JWT must have form header.payload.signature"
      )
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

  private def getAlgorithmFromHeader(header: String): Algorithm = {
    val headerDecoded = Base64Decoder.decodeAsString(header)
    val jwtHeader = headerDecoded.parseJson.convertTo[JwtHeader]
    jwtHeader.algorithm
  }

  private def encode(
      payload: JsValue,
      secret: String,
      algorithm: Algorithm,
      jwtClaims: Option[JwtClaims]
  ): String = {
    val fields = payload.asJsObject.fields
    val reversedClaims = jwtClaims.map(getReversedClaims).getOrElse(Map.empty)

    val payloadWithReservedClaims = JsObject(fields ++ reversedClaims)

    val encodedHeader = getEncodedHeader(algorithm)
    val encodedPayload =
      Base64Encoder.encode(payloadWithReservedClaims.toString)

    val encodedData = s"$encodedHeader.$encodedPayload"

    val signature = algorithm.sign(encodedData, secret)
    s"$encodedData.$signature"
  }

  private def addBouncyCastleProvider(): Unit = {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider)
    }
  }

  private def getEncodedHeader(algorithm: Algorithm): String = {
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
      .map(entry => entry._1 -> entry._2.get)
      .map {
        case (name, value: String) => name -> JsString(value)
        case (name, value: Long)   => name -> JsNumber(value)
        case (name, values: Set[_]) =>
          if (values.size == 1) {
            name -> JsString(values.head.asInstanceOf[String])
          } else {
            name -> JsArray(
              values.map(v => JsString(v.asInstanceOf[String])).toVector
            )
          }

        case (name, _) =>
          throw new SerializationException(
            s"Cannot serialize reserved claim: $name"
          )
      }
      .toMap
  }

}
