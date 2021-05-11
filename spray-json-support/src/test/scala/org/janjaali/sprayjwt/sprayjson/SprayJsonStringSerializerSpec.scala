package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.algorithms.JsonStringSerializerSpec
import org.janjaali.sprayjwt.json.JsonStringSerializer

final class SprayJsonStringSerializerSpec extends JsonStringSerializerSpec:

  override final protected def jsonStringSerializer: JsonStringSerializer =
    SprayJsonStringSerializer

  "SprayJsonStringSerializer" - {

    verifySignWithHmacAlgorithms()
  }
