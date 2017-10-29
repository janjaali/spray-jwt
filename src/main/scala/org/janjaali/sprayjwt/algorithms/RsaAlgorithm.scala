package org.janjaali.sprayjwt.algorithms

import java.io.{IOException, StringReader}
import java.security.{PrivateKey, Signature}

import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.openssl.{PEMKeyPair, PEMParser}
import org.janjaali.sprayjwt.encoder.{Base64Encoder, ByteEncoder}

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

  private def getPrivateKey(str: String): PrivateKey = {
    val pemParser = new PEMParser(new StringReader(str))
    val keyPair = pemParser.readObject()

    Option(keyPair) match {
      case Some(keyPair: PEMKeyPair) =>
        val converter = new JcaPEMKeyConverter
        converter.getKeyPair(keyPair).getPrivate
      case _ => throw new IOException(s"Invalid key for $cryptoAlgName")
    }
  }

}
