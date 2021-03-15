package org.janjaali.sprayjwt.json

/** Provides a JSON writer for a given type T.
  */
trait JsonWriter[T] {

  /** Writes a given value as JSON value.
    *
    * @param value value that should be written as JSON value
    * @return JSON value
    */
  def write(value: T): JsonValue
}
