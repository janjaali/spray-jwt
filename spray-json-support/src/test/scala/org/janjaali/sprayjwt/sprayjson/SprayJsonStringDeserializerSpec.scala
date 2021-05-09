package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.algorithms.JsonStringDeserializerSpec
import org.janjaali.sprayjwt.json.JsonStringDeserializer

final class SprayJsonStringDeserializerSpec extends JsonStringDeserializerSpec:

  override protected def jsonStringDeserializer: JsonStringDeserializer =
    SprayJsonStringDeserializer

  "SprayJsonStringDeserializer" - {

    verifyValidationWithHmacAlgorithms()
  }
