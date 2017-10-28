package org.janjaali.sprayjwt.algorithms

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import org.janjaali.sprayjwt.encoder.{Base64Encoder, ByteEncoder}

/**
  * Companion Object to map Strings to HashingAlgorithms.
  */
object HashingAlgorithm {
  def apply(name: String): Option[HashingAlgorithm] = name match {
    case "HS256" => Some(HS256)
    case _ => None
  }
}

/**
  * Abstract class for HashingAlgorithm.
  *
  * @param name of HashingAlgorithm
  */
sealed abstract class HashingAlgorithm(val name: String) {
  /**
    * Signs data.
    *
    * @param data   to sign
    * @param secret to use for signing
    * @return signed data
    */
  def sign(data: String, secret: String): String
}

/**
  * Represents HS256 HashingAlgorithm.
  */
case object HS256 extends HashingAlgorithm("HS256") {

  private val provider = "SunJCE"

  private val cryptoAlgName = "HMACSHA256"

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