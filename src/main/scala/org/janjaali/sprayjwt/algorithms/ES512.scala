package org.janjaali.sprayjwt.algorithms

case object ES512 extends RsaAlgorithm("ES512") {

  override protected val cryptoAlgName = "SHA512withECDSA"

}
