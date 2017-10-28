package org.janjaali.sprayjwt.algorithms

/**
  * Companion Object to map Strings to HashingAlgorithms.
  */
object HashingAlgorithm {
  def apply(name: String): Option[HashingAlgorithm] = name match {
    case "HS256" => Some(HS256)
    case "HS384" => Some(HS384)
    case "HS512" => Some(HS512)
    case _ => None
  }
}

/**
  * Abstract class for HashingAlgorithm.
  *
  * @param name of HashingAlgorithm
  */
abstract class HashingAlgorithm(val name: String) {
  /**
    * Signs data.
    *
    * @param data   to sign
    * @param secret to use for signing
    * @return signed data
    */
  def sign(data: String, secret: String): String
}
