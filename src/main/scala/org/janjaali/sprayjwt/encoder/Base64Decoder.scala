package org.janjaali.sprayjwt.encoder

import java.util.Base64

/**
  * Simple Base64Decoder.
  */
object Base64Decoder {

  private lazy val base64Decoder: Base64.Decoder = Base64.getDecoder

  /**
    * Decodes Base64 encoded text as byte-array.
    *
    * @param text to decode
    * @return Base64 decoded byte-array
    */
  def decode(text: String): Array[Byte] = {
    base64Decoder.decode(text)
  }

  /**
    * Decodes Base64 decoded text as String.
    *
    * @param text to decode
    * @return Base64 decoded String
    */
  def decodeAsString(text: String): String = {
    new String(decode(text))
  }

}
