package org.janjaali.sprayjwt.exceptions

/**
  * Exception thrown for invalid JWT hashing algorithms.
  *
  * @param message detailed message
  */
class InvalidJwtAlgorithmException(message: String) extends Exception(message)
