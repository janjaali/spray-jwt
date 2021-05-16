package org.janjaali.sprayjwt.jwt

import org.janjaali.sprayjwt.json.{CommonJsonWriters, JsonWriter}

/** A JSON numeric value representing the number of seconds from
  * 1970-01-01T00:00:00Z UTC until the specified UTC date/time, ignoring leap
  * seconds.
  *
  * @param value seconds since the epoch
  */
final case class NumericDate(secondsSinceEpoch: Long)

/** Companion object for Numeric Date.
  */
object NumericDate {

  /** JSON writer for Numeric Date.
    */
  val jsonWriter: JsonWriter[NumericDate] = {

    import CommonJsonWriters.Implicits.longJsonWriter

    CommonJsonWriters.flatValueJsonWriter
  }
}
