package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.algorithms.Algorithm
import org.janjaali.sprayjwt.json.{JsonNumber, JsonObject}
import org.janjaali.sprayjwt.tests.{ScalaCheckGeneratorsSampler, ScalaTestSpec}
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.janjaali.sprayjwt.json.JsonValue

class JoseHeaderSpec extends ScalaTestSpec with ScalaCheckDrivenPropertyChecks {

  "Jose Header" - {

    "when constructed from a sequence of headers" - {

      "should not contain headers with the same name." in {

        forAll(ScalaCheckGenerators.headersGen) { headers =>
          val headerNames = JoseHeader(headers).headers.map(_.name)

          headerNames.toSet.size shouldBe headerNames.size
        }
      }

      "should add headers with distinct names." in {

        forAll(ScalaCheckGenerators.headersGen) { headers =>
          val distinctNamedHeaders = {
            headers.groupBy(_.name).values.map(_.head).toSeq
          }

          val joseHeader = JoseHeader(distinctNamedHeaders)

          joseHeader.headers should contain theSameElementsAs {
            distinctNamedHeaders
          }
        }
      }

      "with headers with the same name" - {

        "should add the later ones." in {

          import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits._

          val joseHeaders = JoseHeader(
            List(Header.Private("name", 1), Header.Private("name", 3))
          )

          joseHeaders.headers should contain only Header.Private("name", 3)
        }
      }
    }

    "when constructed from a JSON object" - {

      "should not contain headers with the same name." in {

        forAll(ScalaCheckGenerators.headersGen) { headers =>

          val json = {
            JsonObject(
              headers.map { header =>
                header.name -> header.valueAsJson
              }.toMap
            )
          }

          val headerNames = JoseHeader(json).headers.map(_.name)

          headerNames.toSet.size shouldBe headerNames.size
        }
      }

      "should add headers with distinct names." in {

        forAll(ScalaCheckGenerators.headersGen) { headers =>
          val distinctNamedHeaders = {
            headers.groupBy(_.name).values.map(_.head).toSeq
          }

          val json = {
            JsonObject(
              distinctNamedHeaders.map { header =>
                header.name -> header.valueAsJson
              }.toMap
            )
          }

          val joseHeader = JoseHeader(json)

          joseHeader.headers.map(_.name) should contain theSameElementsAs {
            distinctNamedHeaders.map(_.name)
          }
        }
      }

      "with headers with the same name" - {

        "should add the later ones." in {

          import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits._

          val joseHeaders = JoseHeader(
            JsonObject(
              Map(
                "name" -> JsonNumber(1),
                "name" -> JsonNumber(3)
              )
            )
          )

          joseHeaders.headers should contain only {
            Header.Private[JsonValue](
              "name",
              JsonNumber(3)
            ) // TODO: JsonNumber(3) should be 3 - grant implicit to deserialize such JSON values in private headers
          }
        }
      }
    }

    "when serialized as JSON" - {

      "should result in a JSON object." in {

        forAll(ScalaCheckGenerators.joseHeader) { joseHeader =>

          val jsonObject = joseHeader.asJson

          val expectedMembers = joseHeader.headers.map { header =>
            header.name -> header.valueAsJson
          }

          jsonObject.members should contain theSameElementsAs expectedMembers
        }
      }
    }
  }
}
