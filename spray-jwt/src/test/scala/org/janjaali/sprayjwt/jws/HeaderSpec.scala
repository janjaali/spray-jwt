package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.json.JsonString
import org.janjaali.sprayjwt.tests.ScalaTestSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class HeaderSpec extends ScalaTestSpec with ScalaCheckDrivenPropertyChecks {

  "Algorithm header" - {

    "when serialized as JSON value" - {

      "should result in a JSON string." in {

        forAll(ScalaCheckGenerators.algorithmHeaderGen) { header =>
          header.valueAsJson shouldBe a[JsonString]
        }
      }
    }
  }

  "Type header" - {

    "when serialized as JSON value" - {

      "should result in a JSON string." in {

        forAll(ScalaCheckGenerators.typeHeaderGen) { header =>
          header.valueAsJson shouldBe a[JsonString]
        }
      }
    }
  }
}
