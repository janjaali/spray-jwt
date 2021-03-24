package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.{algorithms, jwt}
import org.scalacheck.{Arbitrary, Gen}

object ScalaCheckGenerators {

  def algorithmHeaderGen: Gen[JoseHeader.Header.ProtectedHeader.Algorithm] = {

    import JoseHeader.Header.ProtectedHeader.Algorithm

    algorithms.ScalaCheckGenerators.algorithmGen.map(Algorithm.apply)
  }

  def privateProtectedHeaderGen[T: Arbitrary]: Gen[
    JoseHeader.Header.ProtectedHeader.Private[T]
  ] = {

    val valueGen = implicitly[Arbitrary[T]].arbitrary

    for {
      name <- Gen.alphaStr
      value <- valueGen
    } yield JoseHeader.Header.ProtectedHeader.Private(name, value)
  }

  def privateUnprotectedHeaderGen[T: Arbitrary]: Gen[
    JoseHeader.Header.UnprotectedHeader.Private[T]
  ] = {

    val valueGen = implicitly[Arbitrary[T]].arbitrary

    for {
      name <- Gen.alphaStr
      value <- valueGen
    } yield JoseHeader.Header.UnprotectedHeader.Private(name, value)
  }

  def protectedHeaderGen: Gen[JoseHeader.Header.ProtectedHeader] = {
    Gen.oneOf(
      privateProtectedHeaderGen[String],
      algorithmHeaderGen
    )
  }

  def unprotectedHeaderGen: Gen[JoseHeader.Header.UnprotectedHeader] = {
    privateUnprotectedHeaderGen[String]
  }

  def headerGen: Gen[JoseHeader.Header] = {
    Gen.oneOf(
      protectedHeaderGen,
      unprotectedHeaderGen
    )
  }

  def headersGen: Gen[List[JoseHeader.Header]] = {
    Gen.listOf(headerGen)
  }

  def joseHeader: Gen[JoseHeader] = {
    headersGen.map(JoseHeader.apply)
  }

  def jwsPayloadGen: Gen[JwsPayload] = {
    jwt.ScalaCheckGenerators.claimsSetGen.map(JwsPayload.apply)
  }
}
