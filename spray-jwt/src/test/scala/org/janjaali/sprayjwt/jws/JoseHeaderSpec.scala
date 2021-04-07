package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.algorithms.Algorithm
import org.janjaali.sprayjwt.json.JsonObject
import org.janjaali.sprayjwt.tests.{ScalaCheckGeneratorsSampler, ScalaTestSpec}
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class JoseHeaderSpec extends ScalaTestSpec with ScalaCheckDrivenPropertyChecks {

  "Jose Header" - {

    "when constructed" - {

      val sut = JoseHeader.apply _

      "should not contain headers with the same name." in {

        forAll(ScalaCheckGenerators.headersGen) { headers =>
          val headerNames = sut(headers).headers.map(_.name)

          headerNames.toSet.size shouldBe headerNames.size
        }
      }

      "should add all headers with unique names." in {

        forAll(ScalaCheckGenerators.headersGen) { headers =>
          val distinctNamedHeaders = {
            headers.groupBy(_.name).values.map(_.head).toSeq
          }

          val joseHeader = sut(distinctNamedHeaders)

          joseHeader.headers should contain theSameElementsAs {
            distinctNamedHeaders
          }
        }
      }

      "with headers with the same name" - {

        "should add the later ones." in {

          import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits._

          val joseHeaders = sut(
            List(
              Header.Private("name", 1),
              Header.Private("name", 3)
            )
          )

          joseHeaders.headers should contain only Header.Private("name", 3)
        }
      }
    }

    "when serialized as JSON" - {

      "should result in a JSON object." in {

        forAll(ScalaCheckGenerators.joseHeader) { joseHeader =>
          joseHeader.asJson shouldBe a[JsonObject]

          fail("add some more tests for this method.")
        }
      }
    }
  }
}
