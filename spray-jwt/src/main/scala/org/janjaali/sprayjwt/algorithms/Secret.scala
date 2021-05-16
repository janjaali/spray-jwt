package org.janjaali.sprayjwt.algorithms

/** Represents a String based secret value.
  */
final case class Secret(value: String) extends AnyVal {

  /** Encodes this secret value as a byte array using the UTF-8 charset.
    *
    * @return byte-array
    */
  def asByteArray: Array[Byte] = {
    value.getBytes("UTF-8")
  }
}
