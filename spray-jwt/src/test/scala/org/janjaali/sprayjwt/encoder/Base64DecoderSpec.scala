package org.janjaali.sprayjwt.encoder

import org.scalatest.funspec.AnyFunSpec

class Base64DecoderSpec extends AnyFunSpec {

  describe("Base64Decoder") {
    it("decodes text as Base64 decoded byte-array") {
      val decodedByteArray = Base64UrlDecoder.decode("ZGFuY2U=")
      assert(decodedByteArray sameElements "dance".getBytes)
    }
  }

}
