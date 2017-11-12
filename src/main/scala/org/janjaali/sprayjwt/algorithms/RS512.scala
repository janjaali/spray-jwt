package org.janjaali.sprayjwt.algorithms

/**
  * Represents RS512 hashing algorithm.
  */
case object RS512 extends RsaAlgorithm("RS512") {

  override protected val cryptoAlgName = "SHA512withRSA"

}
