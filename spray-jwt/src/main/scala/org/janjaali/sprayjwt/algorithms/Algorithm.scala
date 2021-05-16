package org.janjaali.sprayjwt.algorithms

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.openssl.{PEMKeyPair, PEMParser}
import org.janjaali.sprayjwt.encoder.{
  Base64UrlDecoder,
  Base64UrlEncoder,
  ByteEncoder
}
import org.janjaali.sprayjwt.json.*
import org.janjaali.sprayjwt.jws.{Header, JoseHeader, JwsPayload, JwsSignature}

import java.io.{IOException, StringReader}
import java.security.{PrivateKey, PublicKey, Signature}

/** Represents a cryptographic algorithm used with JWT.
  */
sealed trait Algorithm:

  /** Digitally signs the protected headers of the given Jose Header and the Jws
    * Payload with a given secret.
    *
    * @param joseHeader jose header
    * @param jwsPayload jws payload
    * @param secret secret
    */
  def sign(
      joseHeader: JoseHeader,
      jwsPayload: JwsPayload,
      secret: Secret
  )(using jsonStringSerializer: JsonStringSerializer): JwsSignature

  /** Validates a given JWT token.
    */
  def validate(data: String, secret: Secret): Boolean

/** Algorithms.
  */
object Algorithms:

  /** Hash-based Message Authentication Codes (HMACs) algorithm to sign and
    * validate digital signatures.
    */
  sealed trait Hmac extends Algorithm:

    protected def hashingAlgorithmName: String

    override def sign(
        joseHeader: JoseHeader,
        jwsPayload: JwsPayload,
        secret: Secret
    )(using jsonStringSerializer: JsonStringSerializer): JwsSignature =
      val base64UrlEncodedJoseHeader =
        Base64UrlEncoder.encode(
          jsonStringSerializer.serialize(joseHeader.asJson)
        )

      val base64UrlEncodedJwsPayload =
        Base64UrlEncoder.encode(
          jsonStringSerializer.serialize(jwsPayload.asJson)
        )

      val inputToBeSigned =
        s"$base64UrlEncodedJoseHeader.$base64UrlEncodedJwsPayload"

      sign(inputToBeSigned, secret)

    override def validate(
        data: String,
        secret: Secret
    ): Boolean =
      data.split("\\.") match
        case Array(
              base64UrlEncodedJoseHeader,
              base64UrlEncodedJwsPayload,
              base64EncodedSignature
            ) =>
          val signedInput = {
            s"$base64UrlEncodedJoseHeader.$base64UrlEncodedJwsPayload"
          }

          sign(signedInput, secret).value == base64EncodedSignature

        case _ => false

    private def sign(data: String, secret: Secret): JwsSignature =
      val mac = Mac.getInstance(hashingAlgorithmName)
      val key = new SecretKeySpec(secret.asByteArray, hashingAlgorithmName)

      mac.init(key)

      val signature = mac.doFinal(data.getBytes("UTF-8"))

      JwsSignature(Base64UrlEncoder.encode(signature))

  /** RSASSA-PKCS1-v1_5 (RSA) based algorithm using SHA-2 hash functions to sign
    * and validate digital signatures.
    */
  sealed trait Rsa extends Algorithm:

    private val provider = "BC"

    protected def hashingAlgorithmName: String

    override def sign(
        joseHeader: JoseHeader,
        jwsPayload: JwsPayload,
        secret: Secret
    )(using jsonStringSerializer: JsonStringSerializer): JwsSignature = ???

    override def validate(data: String, secret: Secret): Boolean = ???

    // TODO: Check implementation
    def sign(
        data: String,
        secret: String
    )(using serializeJson: JsonValue => String): String = {

      val key = getPrivateKey(secret)

      val dataByteArray = ByteEncoder.getBytes(data)

      val signature = Signature.getInstance(hashingAlgorithmName, provider)
      signature.initSign(key)
      signature.update(dataByteArray)
      val signatureByteArray = signature.sign
      Base64UrlEncoder.encode(signatureByteArray)
    }

    // TODO: Check implementation
    def validate(
        signature: String,
        data: String,
        secret: String
    )(implicit
        serializeJson: JsonValue => String,
        base64UrlEncoder: Base64UrlEncoder
    ): Boolean = {

      val key = getPublicKey(secret)

      val dataByteArray = ByteEncoder.getBytes(data)

      val rsaSignature = Signature.getInstance(hashingAlgorithmName, provider)
      rsaSignature.initVerify(key)
      rsaSignature.update(dataByteArray)
      rsaSignature.verify(Base64UrlDecoder.decode(signature))
    }

    private def getPublicKey(str: String): PublicKey = {
      val pemParser = new PEMParser(new StringReader(str))
      val keyPair = pemParser.readObject()

      Option(keyPair) match {
        case Some(publicKeyInfo: SubjectPublicKeyInfo) =>
          val converter = new JcaPEMKeyConverter
          converter.getPublicKey(publicKeyInfo)
        case _ =>
          throw new IOException(s"Invalid key for $hashingAlgorithmName")
      }
    }

    private def getPrivateKey(str: String): PrivateKey = {
      val pemParser = new PEMParser(new StringReader(str))
      val keyPair = pemParser.readObject()

      Option(keyPair) match {
        case Some(keyPair: PEMKeyPair) =>
          val converter = new JcaPEMKeyConverter
          converter.getKeyPair(keyPair).getPrivate
        case _ =>
          throw new IOException(s"Invalid key for $hashingAlgorithmName")
      }
    }

  /** HMAC using SHA-256.
    */
  case object Hs256 extends Hmac:
    override val hashingAlgorithmName = "HMACSHA256"

  /** HMAC using SHA-384.
    */
  case object Hs384 extends Hmac:
    override val hashingAlgorithmName = "HMACSHA384"

  /** HMAC using SHA-512.
    */
  case object Hs512 extends Hmac:
    override val hashingAlgorithmName = "HMACSHA512"

  /** RSASSA-PKCS1-v1_5 using SHA-256.
    */
  case object Rs256 extends Rsa:
    override protected def hashingAlgorithmName: String = "SHA256withRSA"

  /** RSASSA-PKCS1-v1_5 using SHA-384.
    */
  case object Rs384 extends Rsa:
    override protected def hashingAlgorithmName: String = "SHA384withRSA"

  /** RSASSA-PKCS1-v1_5 using SHA-512.
    */
  case object Rs512 extends Rsa:
    override protected def hashingAlgorithmName: String = "SHA512withRSA"

  // TODO: Docs.
  // TODO: Test.
  // TODO: Move to the right place.
  def validate(
      data: String,
      secret: Secret // TODO: Do RSA algorithms use the same kind of secret, probably not?
  )(using jsonStringDeserializer: JsonStringDeserializer): Boolean = {

    val maybeAlgorithm = {
      data.split("\\.") match
        case Array(
              base64UrlEncodedJoseHeader,
              base64UrlEncodedJwsPayload,
              base64EncodedSignature
            ) =>
          val maybeJoseHeaderJsonObject = jsonStringDeserializer.deserialize {
            Base64UrlDecoder.decodeAsString {
              base64UrlEncodedJoseHeader
            }
          }

          maybeJoseHeaderJsonObject match {
            case joseHeaderJsonObject: JsonObject =>
              val joseHeader = JoseHeader(joseHeaderJsonObject)

              joseHeader.headers.collectFirst {
                case Header.Algorithm(algorithm) => algorithm
              }
            case _ =>
              // TODO: Log.
              None
          }
        case _ =>
          // TODO: Log.
          None
    }

    maybeAlgorithm match {
      case Some(algorithm) =>
        algorithm.validate(data, secret)
      case None =>
        false
    }
  }
