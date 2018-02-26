package org.janjaali.sprayjwt.encoder

import java.util.Base64

/**
  * Base64Decoder utility class.
  */
private[sprayjwt] object Base64Decoder {

  private lazy val base64Decoder: Base64.Decoder = Base64.getDecoder

  /**
    * Decodes Base64 encoded text as ByteArray.
    *
    * @param text the text to decode as ByteArray
    * @return Base64 decoded ByteArray
    */
  def decode(text: String): Array[Byte] = {
    base64Decoder.decode(text)
  }

  /**
    * Decodes Base64 decoded text as String.
    *
    * @param text the text to decode as String
    * @return Base64 decoded String
    */
  def decodeAsString(text: String): String = {
    new String(decode(text))
  }

}
