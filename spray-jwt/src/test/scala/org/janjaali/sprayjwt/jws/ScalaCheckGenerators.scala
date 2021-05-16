package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.{algorithms, jwt}
import org.scalacheck.{Arbitrary, Gen}
import org.janjaali.sprayjwt.json.JsonWriter

object ScalaCheckGenerators {

  def algorithmHeaderGen: Gen[Header.Algorithm] = {
    algorithms.ScalaCheckGenerators.algorithmGen.map(Header.Algorithm.apply)
  }

  def typeHeaderGen: Gen[Header.Type] = {

    import Header.Type

    Type(Type.Value.Jwt)
  }

  def privateHeaderGen[T: JsonWriter: Arbitrary]: Gen[
    Header.Private[T]
  ] = {

    val valueGen = implicitly[Arbitrary[T]].arbitrary

    for {
      name <- Gen.alphaStr
      value <- valueGen
    } yield Header.Private(name, value)
  }

  def headerGen: Gen[Header] = {

    import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits._

    Gen.oneOf(
      algorithmHeaderGen,
      typeHeaderGen,
      privateHeaderGen[String]
    )
  }

  def headersGen: Gen[List[Header]] = {
    Gen.listOf(headerGen)
  }

  def joseHeader: Gen[JoseHeader] = {
    headersGen.map(JoseHeader.apply)
  }

  def jwsPayloadGen: Gen[JwsPayload] = {
    jwt.ScalaCheckGenerators.claimsSetGen.map(JwsPayload.apply)
  }
}
