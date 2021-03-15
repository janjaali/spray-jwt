package org.janjaali.sprayjwt.jwt.model

import org.janjaali.sprayjwt.tests.ScalaTestSpec
import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits._
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ClaimsSetSpec extends ScalaTestSpec with ScalaCheckDrivenPropertyChecks {

  "Claim Set" - {

    "when constructed with claims that do not share same claim names" - {

      "should add all claims." in {

        val claimsWithDistinctNames = {
          Gen
            .listOf(ScalaCheckGenerators.claimGen)
            .map(_.groupBy(_.name))
            .map(_.mapValues(_.headOption))
            .map(_.values.flatten)
            .map(_.toSeq)
        }

        forAll(claimsWithDistinctNames) { claims =>
          ClaimsSet(claims).claims should contain theSameElementsAs claims
        }
      }
    }

    "when constructed with claims that share same claim names" - {

      val claim1 = Claim.Private("name", 1)
      val claim2 = Claim.Private("name", 2)

      val claims = List(
        claim1,
        claim2
      )

      "should only add the later ones." in {

        ClaimsSet(claims).claims should contain only claim2
      }
    }
  }
}
