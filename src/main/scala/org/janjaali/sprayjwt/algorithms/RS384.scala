package org.janjaali.sprayjwt.algorithms

case object RS384 extends RsaAlgorithm("RS384") {

  override protected val cryptoAlgName = "SHA384withRSA"

}
