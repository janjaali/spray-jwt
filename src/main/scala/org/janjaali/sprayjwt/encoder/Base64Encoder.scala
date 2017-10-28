package org.janjaali.sprayjwt.encoder

import java.util.Base64

/**
  * Simple Base64Encoder.
  */
object Base64Encoder {

  private lazy val base64Encoder: Base64.Encoder = Base64.getEncoder

  /**
    * Encodes text to Base64 encoded String.
    *
    * @param text to encode
    * @return Base64 encoded String
    */
  def encode(text: String): String = {
    val textAsByteArray = ByteEncoder.getBytes(text)
    encode(textAsByteArray)
  }

  /**
    * Encodes Byte-Array as String.
    *
    * @param byteArray to encode
    * @return String encoded Byte-Array
    */
  def encode(byteArray: Array[Byte]): String = {
    base64Encoder.encodeToString(byteArray)
  }

}
