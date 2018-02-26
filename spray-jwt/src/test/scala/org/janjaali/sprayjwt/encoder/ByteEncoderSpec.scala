package org.janjaali.sprayjwt.encoder

import org.scalatest.FunSpec

class ByteEncoderSpec extends FunSpec {

  describe("ByteEncoder") {
    it("encodes text as byte array") {
      val text = "dance"
      val byteArray = ByteEncoder.getBytes(text)
      assert(byteArray sameElements text.getBytes("UTF-8"))
    }
  }

}
