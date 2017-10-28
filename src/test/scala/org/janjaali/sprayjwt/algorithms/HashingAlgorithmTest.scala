package org.janjaali.sprayjwt.algorithms

import org.scalatest.FunSpec

class HashingAlgorithmTest extends FunSpec {

  describe("HashingAlgorithm") {
    describe("maps Strings to HashingAlgorithm") {
      it("maps HS256") {
        HashingAlgorithm("HS256") match {
          case Some(alg) => assert(alg == HS256)
          case _ => fail
        }
      }

      it("maps HS384") {
        HashingAlgorithm("HS384") match {
          case Some(alg) => assert(alg == HS384)
          case _ => fail
        }
      }

      it("maps HS512") {
        HashingAlgorithm("HS512") match {
          case Some(alg) => assert(alg == HS512)
          case _ => fail
        }
      }
    }
  }

}
