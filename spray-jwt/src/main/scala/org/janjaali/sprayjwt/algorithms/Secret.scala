package org.janjaali.sprayjwt.algorithms

/** Represents a String based secret value.
  */
final case class Secret(value: String) extends AnyVal {

  def asByteArray: Array[Byte] = {
    value.getBytes("UTF-8")
  }
}
