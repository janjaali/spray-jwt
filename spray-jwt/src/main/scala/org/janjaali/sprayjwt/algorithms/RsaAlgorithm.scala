package org.janjaali.sprayjwt.algorithms

import java.io.{IOException, StringReader}
import java.security.{PrivateKey, PublicKey, Signature}

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.openssl.{PEMKeyPair, PEMParser}
import org.janjaali.sprayjwt.encoder.{Base64Decoder, Base64Encoder, ByteEncoder}

/** RSASSA-PKCS1-v1_5 (RSA) based algorithm using SHA-2 hash functions to sign
  * and validate digital signatures.
  */
sealed trait RsaAlgorithm extends Algorithm {

  private val provider = "BC"

  protected def hashingAlgorithmName: String

  // TODO: Check implementation
  override def sign(data: String, secret: String): String = {

    val key = getPrivateKey(secret)

    val dataByteArray = ByteEncoder.getBytes(data)

    val signature = Signature.getInstance(hashingAlgorithmName, provider)
    signature.initSign(key)
    signature.update(dataByteArray)
    val signatureByteArray = signature.sign
    Base64Encoder.encode(signatureByteArray)
  }

  // TODO: Check implementation
  override def validate(
      data: String,
      signature: String,
      secret: String
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
      case _ => throw new IOException(s"Invalid key for $hashingAlgorithmName")
    }
  }

  private def getPrivateKey(str: String): PrivateKey = {
    val pemParser = new PEMParser(new StringReader(str))
    val keyPair = pemParser.readObject()

    Option(keyPair) match {
      case Some(keyPair: PEMKeyPair) =>
        val converter = new JcaPEMKeyConverter
        converter.getKeyPair(keyPair).getPrivate
      case _ => throw new IOException(s"Invalid key for $hashingAlgorithmName")
    }
  }
}

object RsaAlgorithm {

  case object Rs256 extends RsaAlgorithm {

    override val name: String = "RS256"

    override protected def hashingAlgorithmName: String = "SHA256withRSA"
  }

  case object Rs384 extends RsaAlgorithm {

    override val name: String = "RS384"

    override protected def hashingAlgorithmName: String = "SHA384withRSA"
  }

  case object Rs512 extends RsaAlgorithm {

    override val name: String = "RS512"

    override protected def hashingAlgorithmName: String = "SHA512withRSA"
  }
}
