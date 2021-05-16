package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.json._
import org.janjaali.sprayjwt.tests.ScalaTestSpec
import spray.json._

final class SprayJsonStringSerializerSpec extends ScalaTestSpec:

  "SprayJsonStringSerializer" - {

    val sut = SprayJsonStringSerializer

    "serializes" - {

      "JSON objects" - {

        "that are empty." in {

          val jsonValue = JsonObject.empty

          sut.serialize(jsonValue) shouldBe "{}"
        }

        "that are not empty." in {

          val jsonValue = JsonObject(
            Map(
              "key" -> JsonString("value"),
              "otherKey" -> JsonNumber(42)
            )
          )

          sut.serialize(jsonValue) shouldBe
            """{"key":"value","otherKey":42}"""
        }
      }

      "JSON arrays" - {

        "that are empty." in {

          val jsonValue = JsonArray.empty

          sut.serialize(jsonValue) shouldBe "[]"
        }

        "that are not empty." in {

          val jsonValue = JsonArray(
            Seq(
              JsonString("value"),
              JsonNumber(42)
            )
          )

          sut.serialize(jsonValue) shouldBe """["value",42]"""
        }
      }

      "JSON strings." in {

        val jsonValue = JsonString("dance")

        sut.serialize(jsonValue) shouldBe "\"dance\""
      }

      "JSON numbers." in {

        val jsonValue = JsonNumber(42)

        sut.serialize(jsonValue) shouldBe "42"
      }

      "JSON booleans." in {

        val jsonValue = JsonBoolean(true)

        sut.serialize(jsonValue) shouldBe "true"
      }

      "JSON nulls." in {

        val jsonValue = JsonNull

        sut.serialize(jsonValue) shouldBe "null"
      }
    }
  }
