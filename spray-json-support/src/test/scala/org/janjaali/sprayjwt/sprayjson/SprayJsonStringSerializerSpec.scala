package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.algorithms.{Algorithm, JsonStringSerializerSpec}
import org.janjaali.sprayjwt.json.JsonValue
import org.janjaali.sprayjwt.json.JsonStringSerializer

final class SprayJsonStringSerializerSpec extends JsonStringSerializerSpec {

  override final protected def jsonStringSerializer: JsonStringSerializer = {
    SprayJsonStringSerializer
  }

  "SprayJsonStringSerializer" - {

    verifySignWithHmac256Algorithm()

    verifySignWithHmac384Algorithm()

    verifySignWithHmac512Algorithm()
  }
}
