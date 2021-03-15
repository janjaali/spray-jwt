package org.janjaali.sprayjwt.headers

import org.janjaali.sprayjwt.algorithms.Algorithm

/**
  * Represents JWT-Header.
  *
  * @param algorithm the hashing algorithm which is used for encoding the JWT
  */
case class JwtHeader(algorithm: Algorithm) {
  /**
    * Default type.
    */
  val typ: String = "JWT"
}
