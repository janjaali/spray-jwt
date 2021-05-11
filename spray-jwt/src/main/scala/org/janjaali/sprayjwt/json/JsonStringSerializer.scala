package org.janjaali.sprayjwt.json

/** Provides a JSON string serializer.
  */
trait JsonStringSerializer:

  /** Gives a JSON string serializer.
    */
  given stringSerializer: (JsonValue => String) with
    def apply(json: JsonValue): String = serialize(json)

  /** Serializes a JSON value as a string.
    *
    * @param json JSON value that should be serialized
    */
  def serialize(json: JsonValue): String
