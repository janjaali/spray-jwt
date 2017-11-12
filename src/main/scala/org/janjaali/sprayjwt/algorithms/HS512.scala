package org.janjaali.sprayjwt.algorithms

/**
  * Represents HS512 hashing algorithm.
  */
case object HS512 extends HmacAlgorithm("HS512") {

  override val cryptoAlgName = "HMACSHA512"

}
