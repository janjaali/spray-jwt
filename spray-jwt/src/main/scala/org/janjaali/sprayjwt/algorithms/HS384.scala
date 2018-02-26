package org.janjaali.sprayjwt.algorithms

/**
  * Represents HS384 hashing algorithm.
  */
case object HS384 extends HmacAlgorithm("HS384") {

  override val cryptoAlgName = "HMACSHA384"

}
