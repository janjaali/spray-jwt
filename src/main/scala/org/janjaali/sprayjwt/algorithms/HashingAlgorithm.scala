package org.janjaali.sprayjwt.algorithms

/**
  * Companion Object to map Strings to HashingAlgorithms.
  */
object HashingAlgorithm {
  def apply(name: String): Option[HashingAlgorithm] = name match {
    case "HS256" => Some(HS256)
    case "HS384" => Some(HS384)
    case "HS512" => Some(HS512)
    case "RS256" => Some(RS256)
    case "RS384" => Some(RS384)
    case "RS512" => Some(RS512)
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

  /**
    * Validates signature.
    *
    * @param data      to validate
    * @param signature to validate
    * @param secret    to use for validation
    * @return <code>true</code> if signature is valid, otherwise returns <code>false</code>
    */
  def validate(data: String, signature: String, secret: String): Boolean
}
