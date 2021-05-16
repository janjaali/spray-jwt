package org.janjaali.sprayjwt.exceptions

/**
  * Exception thrown for invalid JWT's.
  *
  * @param message detailed message
  */
class InvalidJwtException(message: String) extends Exception(message)
