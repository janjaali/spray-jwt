package org.janjaali.sprayjwt.json

// TODO: Docs.

trait JsonStringSerializer {

  final object Implicits {

    implicit def implicitSerialize(json: JsonValue): String = {
      serialize(json)
    }
  }

  def serialize(json: JsonValue): String
}
