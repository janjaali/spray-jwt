package org.janjaali.sprayjwt.exceptions

/**
  * Exception thrown for invalid JWT header.
  *
  * @param message detailed message
  */
class InvalidJwtHeaderException(message: String) extends Exception(message)
