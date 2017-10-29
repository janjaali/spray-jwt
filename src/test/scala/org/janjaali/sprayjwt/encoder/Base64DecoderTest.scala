package org.janjaali.sprayjwt.encoder

import org.scalatest.FunSpec

class Base64DecoderTest extends FunSpec {

  describe("Base64Decoder") {
    it("decodes text as Base64 decoded byte-array") {
      val decodedByteArray = Base64Decoder.decode("ZGFuY2U=")
      assert(decodedByteArray sameElements "dance".getBytes)
    }
  }

}
