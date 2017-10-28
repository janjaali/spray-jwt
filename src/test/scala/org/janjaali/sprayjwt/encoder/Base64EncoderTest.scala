package org.janjaali.sprayjwt.encoder

import org.scalatest.FunSpec

/**
  * @author Siyavash Habashi
  */
class Base64EncoderTest extends FunSpec {

  describe("Base64Encoder") {
    it("encodes text as Base64 encoded text") {
      val encodedString = Base64Encoder.encode("""{"alg":"HS256","typ":"JWT"}""")
      assert(encodedString == "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
    }
  }

}
