package org.janjaali.sprayjwt.algorithms

import org.scalacheck.Gen

object ScalaCheckGenerators {

  def algorithmGen: Gen[Algorithm] = {
    Gen.oneOf(
      Algorithms.Hs256,
      Algorithms.Hs384,
      Algorithms.Hs512,
      Algorithms.Rs256,
      Algorithms.Rs384,
      Algorithms.Rs512
    )
  }
}
