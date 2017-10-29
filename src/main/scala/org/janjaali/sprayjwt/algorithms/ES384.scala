package org.janjaali.sprayjwt.algorithms

case object ES384 extends RsaAlgorithm("ES384") {

  override protected val cryptoAlgName = "SHA384withECDSA"

}
