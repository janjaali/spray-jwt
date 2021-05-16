package org.janjaali.sprayjwt.jwt

import org.janjaali.sprayjwt.json.JsonNumber
import org.janjaali.sprayjwt.tests.ScalaTestSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class NumericDateSpec extends ScalaTestSpec with ScalaCheckDrivenPropertyChecks {

  "Numeric date" - {

    "JSON writer" - {

      val sut = NumericDate.jsonWriter

      "should writer numeric dates as JSON number." in {

        forAll(ScalaCheckGenerators.numericDateGen) { numericDate =>
          sut.write(numericDate) shouldBe {
            JsonNumber(numericDate.secondsSinceEpoch)
          }
        }
      }
    }
  }
}
