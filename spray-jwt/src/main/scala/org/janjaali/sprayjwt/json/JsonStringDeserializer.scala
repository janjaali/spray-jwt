package org.janjaali.sprayjwt.json

/** Provides a JSON string deserializer.
  */
trait JsonStringDeserializer:

  /** Gives a JSON string deserializer.
    */
  given (String => JsonValue) with
    def apply(jsonText: String): JsonValue = deserialize(jsonText)

  /** Deserializes a JSON string as JsonValue.
    *
    * @param jsonText JSON string that should be deserialized
    * @return json value
    */
  def deserialize(jsonText: String): JsonValue
