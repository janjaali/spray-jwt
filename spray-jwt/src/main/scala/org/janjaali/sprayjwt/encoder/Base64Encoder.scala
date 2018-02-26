package org.janjaali.sprayjwt.encoder

import java.util.Base64

/**
  * Base64Encoder utility class.
  */
private[sprayjwt] object Base64Encoder {

  private lazy val base64Encoder: Base64.Encoder = Base64.getEncoder

  /**
    * Encodes text to a Base64 encoded String.
    *
    * @param text the text to encode
    * @return Base64 encoded String
    */
  def encode(text: String): String = {
    val textAsByteArray = ByteEncoder.getBytes(text)
    encode(textAsByteArray)
  }

  /**
    * Encodes a ByteArray as String.
    *
    * @param byteArray the ByteArray to encode as String
    * @return String encoded ByteArray
    */
  def encode(byteArray: Array[Byte]): String = {
    base64Encoder.encodeToString(byteArray).replaceAll("=", "")
  }

}
