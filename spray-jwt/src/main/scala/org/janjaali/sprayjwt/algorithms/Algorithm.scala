package org.janjaali.sprayjwt.algorithms

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.openssl.{PEMKeyPair, PEMParser}
import org.janjaali.sprayjwt.encoder.{Base64Decoder, Base64Encoder, ByteEncoder}
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
      base64encoder: Base64Encoder
  ): JwsSignature

  /** Signs data. // TODO: legacy?
    *
    * @param data   the data to sign
    * @param secret the secret to use for signing the data
    * @return signed data
    */
  def sign(
      data: String,
      secret: String
  )(implicit
      serializer: JsonValue => String,
      base64encoder: Base64Encoder
  ): String

  // TODO: Check if serializer is needed
  // TODO: Idea for new method signature
  // def sign(jwsProtectedHeader: JwsProtectedHeader, jwsPayload: JwsPayload): JwsSignature

  /** Validates signature. // TODO: legacy?
    *
    * @param signature the signature to validate
    * @param data      the data to validate signature for
    * @param secret    the secret to use for validation
    * @return <code>true</code> if signature is valid, otherwise returns <code>false</code>
    */
  def validate(
      signature: String,
      data: String,
      secret: String
  )(implicit
      serializer: JsonValue => String,
      base64encoder: Base64Encoder
  ): Boolean
}

/** Provides algorithms.
  */
object Algorithm {

  /** Hash-based Message Authentication Codes (HMACs) algorithm to sign and
    * validate digital signatures.
    */
  sealed trait Hmac extends Algorithm {

    private val provider = "SunJCE" // TODO: Check if needed to create MAC's

    protected def hashingAlgorithmName: String

    override def sign(
        joseHeader: JoseHeader,
        jwsPayload: JwsPayload,
        secret: Secret
    )(implicit
        serializeJson: JsonValue => String,
        base64encoder: Base64Encoder
    ): JwsSignature = {

      val mac = Mac.getInstance(hashingAlgorithmName, provider)
      val key = new SecretKeySpec(secret.asByteArray, hashingAlgorithmName)

      mac.init(key)

      println(s"JOSE Header JSON: ${serializeJson(joseHeader.asJson)}")

      val base64UrlEncodedJoseHeader = {
        base64encoder.encode {
          serializeJson {
            joseHeader.asJson
          }
        }
      }

      println(s"Base64EncodedJoseHeader: '$base64UrlEncodedJoseHeader'.")

      val base64UrlEncodedJwsPayload = {
        base64encoder.encode {
          serializeJson {
            jwsPayload.asJson
          }
        }
      }

      val inputToBeSigned = {
        s"$base64UrlEncodedJoseHeader.$base64UrlEncodedJwsPayload"
      }

      val signature = mac.doFinal(inputToBeSigned.getBytes("UTF-8"))

      JwsSignature(base64encoder.encode(signature))
    }

    // TODO: Check implementation
    override def sign(
        data: String,
        secret: String
    )(implicit
        serializeJson: JsonValue => String,
        base64encoder: Base64Encoder
    ): String = {

      val secretAsByteArray = ByteEncoder.getBytes(secret)
      val secretKey = new SecretKeySpec(secretAsByteArray, hashingAlgorithmName)

      val dataAsByteArray = ByteEncoder.getBytes(data)

      val mac = Mac.getInstance(hashingAlgorithmName, provider)
      mac.init(secretKey)
      val signAsByteArray = mac.doFinal(dataAsByteArray)
      base64encoder.encode(signAsByteArray)
    }

    // TODO: Check implementation
    // TODO: Check if serializer is needed
    override def validate(
        signature: String,
        data: String,
        secret: String
    )(implicit
        serializeJson: JsonValue => String,
        base64encoder: Base64Encoder
    ): Boolean = {
      sign(data, secret) == signature
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
        base64encoder: Base64Encoder
    ): JwsSignature = {

      ???
    }

    // TODO: Check implementation
    override def sign(
        data: String,
        secret: String
    )(implicit
        serializeJson: JsonValue => String,
        base64encoder: Base64Encoder
    ): String = {

      val key = getPrivateKey(secret)

      val dataByteArray = ByteEncoder.getBytes(data)

      val signature = Signature.getInstance(hashingAlgorithmName, provider)
      signature.initSign(key)
      signature.update(dataByteArray)
      val signatureByteArray = signature.sign
      base64encoder.encode(signatureByteArray)
    }

    // TODO: Check implementation
    override def validate(
        signature: String,
        data: String,
        secret: String
    )(implicit
        serializeJson: JsonValue => String,
        base64encoder: Base64Encoder
    ): Boolean = {

      val key = getPublicKey(secret)

      val dataByteArray = ByteEncoder.getBytes(data)

      val rsaSignature = Signature.getInstance(hashingAlgorithmName, provider)
      rsaSignature.initVerify(key)
      rsaSignature.update(dataByteArray)
      rsaSignature.verify(Base64Decoder.decode(signature))
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
}
