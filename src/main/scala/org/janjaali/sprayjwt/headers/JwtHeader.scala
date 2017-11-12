package org.janjaali.sprayjwt.headers

import org.janjaali.sprayjwt.algorithms.HashingAlgorithm

/**
  * Represents JWT-Header.
  *
  * @param algorithm the hashing algorithm which is used for encoding the JWT
  */
case class JwtHeader(algorithm: HashingAlgorithm) {
  /**
    * Default type.
    */
  val typ: String = "JWT"
}
