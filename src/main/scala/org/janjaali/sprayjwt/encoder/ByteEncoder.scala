package org.janjaali.sprayjwt.encoder


/**
  * Simple ByteEncoder for encoding with UTF-8 charset.
  */
object ByteEncoder {

  private val encodingCharset = "UTF-8"

  /**
    * Encodes text into byte array.
    *
    * @param text to encode
    * @return text encoded as byte array
    */
  def getBytes(text: String): Array[Byte] = {
    text.getBytes(encodingCharset)
  }

}
