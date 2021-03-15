package org.janjaali.sprayjwt.algorithms

import org.scalatest.funspec.AnyFunSpec

class HashingAlgorithmSpec extends AnyFunSpec {

  describe("HashingAlgorithm") {
    describe("maps Strings to HashingAlgorithm") {
      it("maps HS256") {
        Algorithm("HS256") match {
          case Some(alg) => assert(alg == HS256)
          case _ => fail
        }
      }

      it("maps HS384") {
        Algorithm("HS384") match {
          case Some(alg) => assert(alg == HS384)
          case _ => fail
        }
      }

      it("maps HS512") {
        Algorithm("HS512") match {
          case Some(alg) => assert(alg == HS512)
          case _ => fail
        }
      }
    }
  }

}
