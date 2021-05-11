package org.janjaali.sprayjwt.json

// TODO: Docs.

trait JsonStringSerializer {

  object Implicits {

    implicit val implicitSerialize: JsonValue => String = serialize
  }

  def serialize(json: JsonValue): String
}
