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
import org.janjaali.sprayjwt.json.{JsonStringSerializer, JsonValue}
import org.janjaali.sprayjwt.jws.{JoseHeader, JwsPayload, JwsSignature}

import java.io.{IOException, StringReader}
import java.security.{PrivateKey, PublicKey, Signature}

/** Represents a cryptographic algorithm used with JWT.
  */
sealed trait Algorithm {

  /** Digitally signs the protected headers of the given Jose Header and the Jws
    * Payload.
    *
    * @param joseHeader jose header
    * @param jwsPayload jws payload
    * @param secret secret
    */
  def sign(
      joseHeader: JoseHeader,
      jwsPayload: JwsPayload,
      secret: Secret
  )(implicit
      serializeJson: JsonValue => String,
      base64encoder: Base64UrlEncoder
  ): JwsSignature

  // TODO: Add docs.
  def validate(
      data: String,
      secret: Secret
  )(implicit base64UrlEncoder: Base64UrlEncoder): Boolean
}

/** Provides algorithms.
  */
object Algorithm {

  /** Hash-based Message Authentication Codes (HMACs) algorithm to sign and
    * validate digital signatures.
    */
  sealed trait Hmac extends Algorithm {

    protected def hashingAlgorithmName: String

    override def sign(
        joseHeader: JoseHeader,
        jwsPayload: JwsPayload,
        secret: Secret
    )(implicit
        serializeJson: JsonValue => String,
        base64UrlEncoder: Base64UrlEncoder
    ): JwsSignature = {

      val base64UrlEncodedJoseHeader = {
        base64UrlEncoder.encode {
          serializeJson {
            joseHeader.asJson
          }
        }
      }

      val base64UrlEncodedJwsPayload = {
        base64UrlEncoder.encode {
          serializeJson {
            jwsPayload.asJson
          }
        }
      }

      val inputToBeSigned = {
        s"$base64UrlEncodedJoseHeader.$base64UrlEncodedJwsPayload"
      }

      sign(inputToBeSigned, secret)
    }

    override def validate(
        data: String,
        secret: Secret
    )(implicit base64UrlEncoder: Base64UrlEncoder): Boolean = {

      data.split("\\.") match {
        case Array(
              base64UrlEncodedJoseHeader,
              base64UrlEncodedJwsPayload,
              base64EncodedSignature
            ) =>
          val signedInput = {
            s"$base64UrlEncodedJoseHeader.$base64UrlEncodedJwsPayload"
          }

          sign(signedInput, secret).value == base64EncodedSignature

        case _ =>
          false
      }
    }

    private def sign(
        data: String,
        secret: Secret
    )(implicit base64UrlEncoder: Base64UrlEncoder): JwsSignature = {

      val mac = Mac.getInstance(hashingAlgorithmName)
      val key = new SecretKeySpec(secret.asByteArray, hashingAlgorithmName)

      mac.init(key)

      val signature = mac.doFinal(data.getBytes("UTF-8"))

      JwsSignature(base64UrlEncoder.encode(signature))
    }
  }

  /** Provides HMAC algorithms.
    */
  object Hmac {

    /** HMAC using SHA-256
      */
    final case object Hs256 extends Hmac {
      override val hashingAlgorithmName = "HMACSHA256"
    }

    /** HMAC using SHA-384
      */
    final case object Hs384 extends Hmac {
      override val hashingAlgorithmName = "HMACSHA384"
    }

    /** HMAC using SHA-512
      */
    final case object Hs512 extends Hmac {
      override val hashingAlgorithmName = "HMACSHA512"
    }
  }

  /** RSASSA-PKCS1-v1_5 (RSA) based algorithm using SHA-2 hash functions to sign
    * and validate digital signatures.
    */
  sealed trait Rsa extends Algorithm {

    private val provider = "BC"

    protected def hashingAlgorithmName: String

    override def sign(
        joseHeader: JoseHeader,
        jwsPayload: JwsPayload,
        secret: Secret
    )(implicit
        serializeJson: JsonValue => String,
        base64UrlEncoder: Base64UrlEncoder
    ): JwsSignature = {

      ???
    }

    // TODO: Check implementation
    def sign(
        data: String,
        secret: String
    )(implicit
        serializeJson: JsonValue => String,
        base64UrlEncoder: Base64UrlEncoder
    ): String = {

      val key = getPrivateKey(secret)

      val dataByteArray = ByteEncoder.getBytes(data)

      val signature = Signature.getInstance(hashingAlgorithmName, provider)
      signature.initSign(key)
      signature.update(dataByteArray)
      val signatureByteArray = signature.sign
      base64UrlEncoder.encode(signatureByteArray)
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

    override def validate(
        data: String,
        secret: Secret
    )(implicit base64encoder: Base64UrlEncoder): Boolean = {
      ???
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
  }

  /** Provides RSA algorithms.
    */
  object Rsa {

    /** RSASSA-PKCS1-v1_5 using SHA-256
      */
    final case object Rs256 extends Rsa {
      override protected def hashingAlgorithmName: String = "SHA256withRSA"
    }

    /** RSASSA-PKCS1-v1_5 using SHA-384
      */
    final case object Rs384 extends Rsa {
      override protected def hashingAlgorithmName: String = "SHA384withRSA"
    }

    /** RSASSA-PKCS1-v1_5 using SHA-512
      */
    final case object Rs512 extends Rsa {
      override protected def hashingAlgorithmName: String = "SHA512withRSA"
    }
  }

  def validate(
      data: String,
      secret: Secret // TODO: Do RSA algorithms use the same secret, probably not?
  )(implicit
      deserializeJson: String => JsonValue,
      base64UrlDecoder: Base64UrlDecoder
  ): Boolean = {

    data.split("\\.") match {
      case Array(
            base64UrlEncodedJoseHeader,
            base64UrlEncodedJwsPayload,
            base64EncodedSignature
          ) =>
        val joseHeaderJson = deserializeJson {
          base64UrlDecoder.decodeAsString {
            base64UrlEncodedJoseHeader
          }
        }

        ??? // TODO: Continue here!

      case _ =>
        false
    }
  }
}
