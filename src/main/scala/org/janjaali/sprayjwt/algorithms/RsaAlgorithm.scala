package org.janjaali.sprayjwt.algorithms

import java.security.spec.PKCS8EncodedKeySpec
import java.security.{KeyFactory, PrivateKey, Signature}

import org.janjaali.sprayjwt.encoder.{Base64Decoder, Base64Encoder, ByteEncoder}

/**
  * Represents RsaAlgorithm.
  */
abstract class RsaAlgorithm(override val name: String) extends HashingAlgorithm(name) {

  private val provider = "BC"

  /**
    * Defines algorithm name used by provider.
    */
  protected val cryptoAlgName: String

  override def sign(data: String, secret: String): String = {
    val key = getPrivateKey(secret)

    val dataByteArray = ByteEncoder.getBytes(data)

    val signature = Signature.getInstance(cryptoAlgName, provider)
    signature.initSign(key)
    signature.update(dataByteArray)
    val signatureByteArray = signature.sign
    Base64Encoder.encode(signatureByteArray)
  }

  def parseRsaKey(secret: String): Array[Byte] = {
    val pureSecret = secret.replaceAll("-----BEGIN (.*)-----", "")
      .replaceAll("-----END (.*)-----", "")
      .replaceAll("\r\n", "")
      .replaceAll("\n", "")
      .trim

    Base64Decoder.decode(pureSecret)
  }

  private def getPrivateKey(secret: String): PrivateKey = {
    val key = parseRsaKey(secret)
    val keySpec = new PKCS8EncodedKeySpec(key)

    val keyFactory = KeyFactory.getInstance("RSA", provider)
    keyFactory.generatePrivate(keySpec)
  }

}
