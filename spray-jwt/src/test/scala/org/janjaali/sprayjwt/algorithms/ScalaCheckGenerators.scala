package org.janjaali.sprayjwt.algorithms

import org.scalacheck.Gen

object ScalaCheckGenerators {

  def rsaAlgorithmGen: Gen[Algorithm.Rsa] = {
    Gen.oneOf(
      Algorithm.Rsa.Rs256,
      Algorithm.Rsa.Rs384,
      Algorithm.Rsa.Rs512
    )
  }

  def hmacAlgorithmGen: Gen[Algorithm.Hmac] = {
    Gen.oneOf(
      Algorithm.Hmac.Hs256,
      Algorithm.Hmac.Hs384,
      Algorithm.Hmac.Hs512
    )
  }

  def algorithmGen: Gen[Algorithm] = {
    Gen.oneOf(
      hmacAlgorithmGen,
      rsaAlgorithmGen
    )
  }
}
