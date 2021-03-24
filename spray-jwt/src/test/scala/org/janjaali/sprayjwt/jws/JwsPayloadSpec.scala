package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.tests.ScalaTestSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class JwsPayloadSpec extends ScalaTestSpec with ScalaCheckDrivenPropertyChecks {

  "JWS payload" - {

    "should serialize claims as JSON object." in {

      forAll(ScalaCheckGenerators.jwsPayloadGen) { jwsPayload =>
        val jwsPayloadAsJsonObject = jwsPayload.asJson

        val expectedJsonObjectMembers = {

          jwsPayload.claimsSet.claims.map { claim =>
            claim.name -> claim.valueAsJson
          }.toMap
        }

        jwsPayloadAsJsonObject.members should contain theSameElementsAs expectedJsonObjectMembers
      }
    }
  }
}
