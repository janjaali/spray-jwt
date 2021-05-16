package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.json._
import org.janjaali.sprayjwt.tests.ScalaTestSpec
import spray.json._

final class SprayJsonStringDeserializerSpec extends ScalaTestSpec:

  "SprayJsonStringDeserializer" - {

    val sut = SprayJsonStringDeserializer

    "deserializes" - {

      "JSON objects" - {

        "that are empty." in {

          val jsonText = "{}"

          sut.deserialize(jsonText) shouldBe JsonObject.empty
        }

        "that are not empty." in {

          val jsonText = """{
            | "key":"value",
            | "otherKey":42}
            |""".stripMargin

          sut.deserialize(jsonText) shouldBe JsonObject(
            Map(
              "key" -> JsonString("value"),
              "otherKey" -> JsonNumber(42)
            )
          )
        }
      }

      "JSON arrays" - {

        "that are empty." in {

          val jsonText = "[]"

          sut.deserialize(jsonText) shouldBe JsonArray.empty
        }

        "that are not empty." in {

          val jsonText = """["value",42]"""

          sut.deserialize(jsonText) shouldBe JsonArray(
            Seq(
              JsonString("value"),
              JsonNumber(42)
            )
          )
        }
      }

      "JSON strings." in {

        val jsonText = "\"dance\""

        sut.deserialize(jsonText) shouldBe JsonString("dance")
      }

      "JSON numbers." in {

        val jsonText = "42"

        sut.deserialize(jsonText) shouldBe JsonNumber(42)
      }

      "JSON booleans." in {

        val jsonText = "true"

        sut.deserialize(jsonText) shouldBe JsonBoolean(true)
      }

      "JSON nulls." in {

        val jsonText = "null"

        sut.deserialize(jsonText) shouldBe JsonNull
      }
    }
  }
