package org.janjaali.sprayjwt.encoder

import java.util.Base64

/** Default Base64 URL decoder that uses internally the [[java.util.Base64]] URL
  * decoder.
  */
private[sprayjwt] trait Base64UrlDecoder {

  private lazy val decoder: Base64.Decoder = Base64.getUrlDecoder

  /** Decodes Base64 URL encoded text as ByteArray.
    *
    * @param text text that should be decoded
    * @return Base64 URL decoded ByteArray
    */
  def decode(text: String): Array[Byte] = {
    decoder.decode(text)
  }

  /** Decodes Base64 URL encoded text as String.
    *
    * @param text text that should be decoded
    * @return Base64 URL decoded String
    */
  def decodeAsString(text: String): String = {
    new String(decode(text))
  }
}

/** Base64 URL decoder utilities.
  */
private[sprayjwt] object Base64UrlDecoder extends Base64UrlDecoder
