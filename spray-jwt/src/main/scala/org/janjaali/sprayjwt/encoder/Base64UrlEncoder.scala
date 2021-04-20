package org.janjaali.sprayjwt.encoder

import java.util.Base64

/** Default Base64 URL encoder that uses internally the [[java.util.Base64]] URL
  * encoder.
  */
private[sprayjwt] trait Base64UrlEncoder {

  private lazy val encoder: Base64.Encoder = Base64.getUrlEncoder.withoutPadding

  /** Encodes text to a Base64 URL encoded String.
    *
    * @param text text that should be encoded
    * @return Base64 URL encoded String
    */
  def encode(text: String): String = {
    encode(text.getBytes("UTF-8"))
  }

  /** Encodes a byte-array as Base64 URL encoded String.
    *
    * @param byteArray ByteArray to encode as String
    * @return String encoded ByteArray
    */
  def encode(byteArray: Array[Byte]): String = {
    encoder.encodeToString(byteArray)
  }
}

/** Base64 URL encoder utilities.
  */
private[sprayjwt] object Base64UrlEncoder extends Base64UrlEncoder
