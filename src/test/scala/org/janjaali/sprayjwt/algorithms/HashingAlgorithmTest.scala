package org.janjaali.sprayjwt.algorithms

import org.scalatest.FunSpec

class HashingAlgorithmTest extends FunSpec {

  describe("HashingAlgorithm") {
    describe("HS256") {
      it("is map-able from String HS256") {
        HashingAlgorithm("HS256") match {
          case Some(alg) => assert(alg == HS256)
          case _ => fail
        }
      }
    }
  }

}
