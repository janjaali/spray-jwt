package org.janjaali.sprayjwt.algorithms

case object ES256 extends RsaAlgorithm("ES256") {

  override protected val cryptoAlgName = "SHA256withECDSA"

}
