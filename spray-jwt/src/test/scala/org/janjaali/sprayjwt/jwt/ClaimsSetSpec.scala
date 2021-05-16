package org.janjaali.sprayjwt.jwt

import org.janjaali.sprayjwt.tests.ScalaTestSpec
import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits._
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ClaimsSetSpec extends ScalaTestSpec with ScalaCheckDrivenPropertyChecks {

  "Claim Set" - {

    "when constructed" - {

      val sut = JwtClaimsSet.apply _

      "should not contain claims with the same name." in {

        forAll(ScalaCheckGenerators.claimsGen) { claims =>
          val headerNames = sut(claims).claims.map(_.name)

          headerNames.toSet.size shouldBe headerNames.size
        }
      }

      "should add all claims with uniqueNames." in {

        forAll(ScalaCheckGenerators.claimsGen) { claims =>
          val distinctNamedClaims = {
            claims.groupBy(_.name).values.map(_.head).toSeq
          }

          val claimsSet = sut(distinctNamedClaims)

          claimsSet.claims should contain theSameElementsAs {
            distinctNamedClaims
          }
        }
      }

      "with claims with the same name" - {

        "should add the later ones." in {
          val claimsSet = sut(
            List(
              Claim.Private("name", 1),
              Claim.Private("name", 4)
            )
          )

          claimsSet.claims should contain only {
            Claim.Private("name", 4)
          }
        }
      }
    }
  }
}
