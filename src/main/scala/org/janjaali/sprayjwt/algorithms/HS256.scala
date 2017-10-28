package org.janjaali.sprayjwt.algorithms

/**
  * Represents HS256 HashingAlgorithm.
  */
case object HS256 extends HmacAlgorithm("HS256") {

  override val cryptoAlgName = "HMACSHA256"

}
