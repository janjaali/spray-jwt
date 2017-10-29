package org.janjaali.sprayjwt.algorithms

/**
  * Represents RS256 HashingAlgorithm.
  */
case object RS256 extends RsaAlgorithm("RS256") {

  override protected val cryptoAlgName = "SHA256withRSA"

}
