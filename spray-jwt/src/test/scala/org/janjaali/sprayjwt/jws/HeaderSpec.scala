package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.algorithms
import org.janjaali.sprayjwt.algorithms.Algorithms
import org.janjaali.sprayjwt.json.CommonJsonWriters.Implicits.jsonValueJsonWriter
import org.janjaali.sprayjwt.json.{JsonBoolean, JsonString, JsonValue}
import org.janjaali.sprayjwt.tests.ScalaCheckGeneratorsSampler._
import org.janjaali.sprayjwt.tests.ScalaTestSpec
import org.scalacheck.Gen
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

  "Header" - {

    "when constructed from a header name and value" - {

      "when header name is 'alg'" - {

        val headerName = "alg"

        "when value matches an 'RS256'" - {

          behave like createAlgorithmHeader(
            value = JsonString("RS256"),
            expectedHeader = Header.Algorithm(Algorithms.Rs256)
          )
        }

        "when value matches an 'RS384'" - {

          behave like createAlgorithmHeader(
            value = JsonString("RS384"),
            expectedHeader = Header.Algorithm(Algorithms.Rs384)
          )
        }

        "when value matches an 'RS512'" - {

          behave like createAlgorithmHeader(
            value = JsonString("RS512"),
            expectedHeader = Header.Algorithm(Algorithms.Rs512)
          )
        }

        "when value matches an 'HS256'" - {

          behave like createAlgorithmHeader(
            value = JsonString("HS256"),
            expectedHeader = Header.Algorithm(Algorithms.Hs256)
          )
        }

        "when value matches an 'HS384'" - {

          behave like createAlgorithmHeader(
            value = JsonString("HS384"),
            expectedHeader = Header.Algorithm(Algorithms.Hs384)
          )
        }

        "when value matches an 'HS512'" - {

          behave like createAlgorithmHeader(
            value = JsonString("HS512"),
            expectedHeader = Header.Algorithm(Algorithms.Hs512)
          )
        }

        def createAlgorithmHeader(
            value: JsonString,
            expectedHeader: Header.Algorithm
        ): Unit = {

          s"should create $expectedHeader." in {
            Header(headerName, value) shouldBe expectedHeader
          }
        }

        "when value does not match an algorithm" - {

          "should create a private header." in {

            Header(headerName, JsonBoolean(false)) shouldBe {
              Header.Private[JsonValue](headerName, JsonBoolean(false))
            }
          }
        }
      }

      "when header name is 'typ'" - {

        val headerName = "typ"

        "when value matches a type value" - {

          "should create a type header." in {

            Header(headerName, JsonString("JWT")) shouldBe {
              Header.Type(Header.Type.Value.Jwt)
            }
          }
        }

        "when value does not match a type value" - {

          "should create a private header." in {

            Header(headerName, JsonBoolean(false)) shouldBe {
              Header.Private[JsonValue](headerName, JsonBoolean(false))
            }
          }
        }
      }

      "when header name does not match 'alg' nor 'typ'" - {

        "should create a private header." in {

          Header("arbitrary", JsonBoolean(false)) shouldBe {
            Header.Private[JsonValue]("arbitrary", JsonBoolean(false))
          }
        }
      }
    }
  }
}
