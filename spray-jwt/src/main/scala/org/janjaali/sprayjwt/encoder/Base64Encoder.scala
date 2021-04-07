package org.janjaali.sprayjwt.encoder

import java.util.Base64
import org.janjaali.sprayjwt.json.{JsonStringSerializer, JsonValue}

/** Base64Encoder utility class.
  */
private[sprayjwt] object Base64Encoder {

  private lazy val base64Encoder: Base64.Encoder = Base64.getEncoder

  /** Encodes text to a Base64 encoded String.
    *
    * @param text the text to encode
    * @return Base64 encoded String
    */
  def encode(text: String): String = {
    val textAsByteArray = text.getBytes("UTF-8")
    encode(textAsByteArray)
  }

  /** Encodes JSON value to a Base64 encoded String.
    *
    * @param jsonValue json value that should be encoded
    * @param jsonStringSerializer Serializer that is used to serialize the json
    *                             value to a String before Base64 encoding sets
    *                             in
    * @return Base64 encoded JSON value
    */
  def encode(
      jsonValue: JsonValue
  )(implicit jsonStringSerializer: JsonStringSerializer): String = {
    jsonStringSerializer.serialize(jsonValue)
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
