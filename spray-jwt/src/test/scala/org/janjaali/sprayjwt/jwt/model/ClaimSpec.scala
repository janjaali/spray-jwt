package org.janjaali.sprayjwt.jwt.model

import org.janjaali.sprayjwt.json.JsonNumber
import org.janjaali.sprayjwt.tests.ScalaTestSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ClaimSpec extends ScalaTestSpec with ScalaCheckPropertyChecks {

  "Expiration time claim" - {

    "value should be JSON serialized as a JSON number." in {

      forAll(ScalaCheckGenerators.expirationTimeClaimGen) { claim =>
        claim.valueAsJson shouldBe JsonNumber(claim.value.secondsSinceEpoch)
      }
    }
  }
}
