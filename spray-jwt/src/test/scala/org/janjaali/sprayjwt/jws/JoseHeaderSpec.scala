package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.algorithms.Algorithm
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

      "with unprotected headers with the same name" - {

        "should add the later ones." in {

          val joseHeaders = sut(
            List(
              JoseHeader.Header.UnprotectedHeader.Private("name", 1),
              JoseHeader.Header.UnprotectedHeader.Private("name", 3)
            )
          )

          joseHeaders.headers should contain only {
            JoseHeader.Header.UnprotectedHeader.Private("name", 3)
          }
        }
      }

      "with protected headers with the same name" - {

        "should add the later ones." in {

          val joseHeaders = sut(
            List(
              JoseHeader.Header.ProtectedHeader.Private("name", 1),
              JoseHeader.Header.ProtectedHeader.Private("name", 3)
            )
          )

          joseHeaders.headers should contain only {
            JoseHeader.Header.ProtectedHeader.Private("name", 3)
          }
        }
      }

      "with protected and unprotected headers with the same name" - {

        "should add the protected ones." in {

          val joseHeaders = sut(
            List(
              JoseHeader.Header.ProtectedHeader.Private("name", 1),
              JoseHeader.Header.UnprotectedHeader.Private("name", 3)
            )
          )

          joseHeaders.headers should contain only {
            JoseHeader.Header.ProtectedHeader.Private("name", 1)
          }
        }
      }
    }

    "should return all headers." in {

      forAll(ScalaCheckGenerators.joseHeader) { joseHeader =>
        joseHeader.headers should contain theSameElementsAs {
          joseHeader.protectedHeaders ++ joseHeader.unprotectedHeaders
        }
      }
    }
  }
}
