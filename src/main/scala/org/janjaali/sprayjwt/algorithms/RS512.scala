package org.janjaali.sprayjwt.algorithms

case object RS512 extends RsaAlgorithm("RS512") {

  override protected val cryptoAlgName = "SHA512withRSA"

}
