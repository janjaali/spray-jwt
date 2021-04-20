package org.janjaali.sprayjwt.encoder

import java.util.Base64
import org.janjaali.sprayjwt.json.{JsonStringSerializer, JsonValue}

// TODO: Remove JSON stuff out of here.

// TODO: Probably rename to Base64UrlEncoder.

sealed trait Base64Encoder {

  private lazy val base64Encoder: Base64.Encoder = Base64.getUrlEncoder

  /** Encodes text to a Base64 encoded String.
    *
    * @param text the text to encode
    * @return Base64 encoded String
    */
  def encode(text: String): String = {
    val textAsByteArray = text.getBytes("UTF-8")
    encode(textAsByteArray)
  }

  /** Encodes a ByteArray as String.
    *
    * @param byteArray the ByteArray to encode as String
    * @return String encoded ByteArray
    */
  def encode(byteArray: Array[Byte]): String = {
    base64Encoder.encodeToString(byteArray).replaceAll("=", "")
  }
}

/** Base64Encoder utility class.
  */
private[sprayjwt] object Base64Encoder extends Base64Encoder // TODO: Check if needed
