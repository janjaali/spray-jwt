package org.janjaali.sprayjwt.algorithms

/**
  * Represents HS256 hashing algorithm.
  */
case object HS256 extends HmacAlgorithm("HS256") {

  override val cryptoAlgName = "HMACSHA256"

}
