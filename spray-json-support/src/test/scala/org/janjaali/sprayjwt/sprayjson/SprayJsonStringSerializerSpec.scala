package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.algorithms.{Algorithm, JsonStringSerializerSpec}
import org.janjaali.sprayjwt.json.{JsonStringSerializer, JsonValue}

final class SprayJsonStringSerializerSpec extends JsonStringSerializerSpec {

  override final protected def jsonStringSerializer: JsonStringSerializer = {
    SprayJsonStringSerializer
  }

  "SprayJsonStringSerializer" - {

    verifySignWithHmacAlgorithms()
  }
}
