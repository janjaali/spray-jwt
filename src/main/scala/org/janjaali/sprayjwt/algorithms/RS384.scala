package org.janjaali.sprayjwt.algorithms

/**
  * Represents RS384 hashing algorithm.
  */
case object RS384 extends RsaAlgorithm("RS384") {

  override protected val cryptoAlgName = "SHA384withRSA"

}
