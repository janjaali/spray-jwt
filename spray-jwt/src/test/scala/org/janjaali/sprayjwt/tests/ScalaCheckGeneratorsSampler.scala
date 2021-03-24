package org.janjaali.sprayjwt.tests

import org.scalacheck.Gen

/** Provides sampler based on ScalaCheck generators and enables to do the
  * following:
  *
  * <pre>
  * val gen: Gen[T] = ???
  * val t: T = gen.get
  * </pre>
  */
trait ScalaCheckGeneratorsSampler {

  implicit class Sampler[T](gen: Gen[T]) {

    def get: T = {
      gen.sample.get
    }
  }
}
