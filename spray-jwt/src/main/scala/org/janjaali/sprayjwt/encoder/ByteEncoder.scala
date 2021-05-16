package org.janjaali.sprayjwt.encoder

/**
  * ByteEncoder utility class.
  */
private[sprayjwt] object ByteEncoder {

  private val encodingCharset = "UTF-8"

  // TODO: Check usage?

  /**
    * Encodes text into a byte array used UTF-8 charset.
    *
    * @param text the text to encode
    * @return encoded byte array
    */
  def getBytes(text: String): Array[Byte] = {
    text.getBytes(encodingCharset)
  }

}
