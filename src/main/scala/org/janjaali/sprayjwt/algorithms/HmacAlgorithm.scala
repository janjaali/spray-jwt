package org.janjaali.sprayjwt.algorithms

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import org.janjaali.sprayjwt.encoder.{Base64Encoder, ByteEncoder}

/**
  * Represents HmacAlgorithm.
  */
abstract class HmacAlgorithm(override val name: String) extends HashingAlgorithm(name) {

  private val provider = "SunJCE"

  /**
    * Defines algorithm name used by SunJCE.
    */
  protected val cryptoAlgName: String

  override def sign(data: String, secret: String): String = {
    val secretAsByteArray = ByteEncoder.getBytes(secret)
    val secretKey = new SecretKeySpec(secretAsByteArray, cryptoAlgName)

    val dataAsByteArray = ByteEncoder.getBytes(data)

    val mac = Mac.getInstance(cryptoAlgName, provider)
    mac.init(secretKey)
    val signAsByteArray = mac.doFinal(dataAsByteArray)
    Base64Encoder.encode(signAsByteArray)
  }

}
