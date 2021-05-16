package org.janjaali.sprayjwt.json

import org.janjaali.sprayjwt.tests.ScalaTestSpec

class CommonJsonWritersSpec extends ScalaTestSpec {

  private val sut = CommonJsonWriters

  "Int JsonWriter" - {

    "should write Int values as JsonNumber." in {

      sut.intJsonWriter.write(2) shouldBe JsonNumber(2)
    }
  }

  "Long JsonWriter" - {

    "should write Long values as JsonNumber." in {

      sut.longJsonWriter.write(41L) shouldBe JsonNumber(41L)
    }
  }

  "String JsonWriter" - {

    "should write String values as JsonString." in {

      sut.stringJsonWriter.write("string") shouldBe JsonString("string")
    }
  }

  "Flat value JSON writer" - {

    "should write flat JSON values for products with at least an arbitrary of 1." in {

      import CommonJsonWriters.Implicits.stringJsonWriter

      case class Prod(value: String)

      val prodExample = Prod("dance")

      sut.flatValueJsonWriter[Prod, String].write(prodExample) shouldBe {
        JsonString("dance")
      }
    }
  }
}
