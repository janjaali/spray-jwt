package org.janjaali.sprayjwt.algorithms

/**
  * Companion object to map Strings as hashing algorithms.
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
  * Represents hashing algorithms used for JWT's.
  *
  * @param name the name of the hashing algorithm
  */
private[sprayjwt] abstract class HashingAlgorithm(val name: String) {
  /**
    * Signs data.
    *
    * @param data   the data to sign
    * @param secret the secret to use for signing the data
    * @return signed data
    */
  def sign(data: String, secret: String): String

  /**
    * Validates signature.
    *
    * @param data      the data to validate signature for
    * @param signature the signature to validate
    * @param secret    the secret to use for validation
    * @return <code>true</code> if signature is valid, otherwise returns <code>false</code>
    */
  def validate(data: String, signature: String, secret: String): Boolean
}
