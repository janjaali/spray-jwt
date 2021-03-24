package org.janjaali.sprayjwt.jwt

import org.janjaali.sprayjwt.json.CommonJsonWriters
import org.scalacheck.Gen

import java.time.Instant

private[jwt] object ScalaCheckGenerators {

  /** Generator for numeric dates containing epoch seconds in the time frame of:
    * [now - 100 years, now + 100 years].
    *
    * @return generator for numeric date
    */
  def numericDateGen: Gen[NumericDate] = {

    import scala.concurrent.duration._

    val aHundredYearsInSeconds = (365.days * 10).toSeconds

    val now = Instant.now()

    Gen
      .chooseNum(
        now.minusSeconds(aHundredYearsInSeconds).getEpochSecond(),
        now.plusSeconds(aHundredYearsInSeconds).getEpochSecond()
      )
      .map(NumericDate.apply)
  }

  /** Generator for an expiration time claim that contains a numeric date time
    * in the time interval of: [now - 100 years, now + 100 years].
    *
    * @return generator for expiration time
    */
  def expirationTimeClaimGen: Gen[Claim.ExpirationTime] = {
    numericDateGen.map(Claim.ExpirationTime)
  }

  def privateClaimGen: Gen[Claim.Private[_]] = {

    import CommonJsonWriters.Implicits.stringJsonWriter

    for {
      name <- Gen.alphaStr
      value <- Gen.alphaStr
    } yield Claim.Private(name, value)
  }

  def claimGen: Gen[Claim] = {

    Gen.oneOf(
      expirationTimeClaimGen,
      privateClaimGen
    )
  }

  def claimsGen: Gen[List[Claim]] = {
    Gen.listOf(claimGen)
  }

  def claimsSetGen: Gen[ClaimsSet] = {
    claimsGen.map(ClaimsSet.apply)
  }

  def jwsPayloadGen: Gen[JwsPayload] = {
    claimsSetGen.map(JwsPayload.apply)
  }
}
