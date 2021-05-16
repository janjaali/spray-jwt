package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.algorithms.JsonSupportSpec
import org.janjaali.sprayjwt.json.JsonStringSerializer
import org.janjaali.sprayjwt.json.JsonStringDeserializer

final class SprayJsonSupportSpec extends JsonSupportSpec:

  override protected given jsonStringSerializer: JsonStringSerializer =
    SprayJsonStringSerializer

  override protected given jsonStringDeserializer: JsonStringDeserializer =
    SprayJsonStringDeserializer

  "spray-json support" - {

    verifySignWithHmacAlgorithms()

    verifyValidationWithHmacAlgorithms()
  }
