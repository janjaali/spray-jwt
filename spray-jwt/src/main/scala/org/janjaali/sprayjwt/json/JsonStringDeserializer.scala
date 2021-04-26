package org.janjaali.sprayjwt.json

// TODO: Docs.

trait JsonStringDeserializer {

  final object Implicits {

    implicit val implicitDeserialize: String => JsonValue = deserialize
  }

  def deserialize(jsonText: String): JsonValue
}
