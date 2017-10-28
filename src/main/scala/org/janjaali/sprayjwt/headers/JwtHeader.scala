package org.janjaali.sprayjwt.headers

import org.janjaali.sprayjwt.algorithms.HashingAlgorithm

/**
  * Represents JWT-Header.
  *
  * @param algorithm used for encoding JWT
  */
case class JwtHeader(algorithm: HashingAlgorithm) {
  /**
    * Default type.
    */
  val typ: String = "JWT"
}
