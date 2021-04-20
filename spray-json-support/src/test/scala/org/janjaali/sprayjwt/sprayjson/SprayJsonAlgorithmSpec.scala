package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.algorithms.{Algorithm, AlgorithmSpec}

final class SprayJsonAlgorithmSpec extends AlgorithmSpec {

  import SprayJsonStringSerializer.Implicits._

  "SprayJsonAlgorithm" - {

    behave like signWithHmacAlgorithm(
      Algorithm.Hmac.Hs256
    )
  }
}
