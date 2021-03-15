package org.janjaali.sprayjwt.jwt.model

import org.janjaali.sprayjwt.json.{JsonValue, JsonWriter}

/** A claim consists of a name-value pair that provide information about a token
  * subject.
  */
trait Claim {

  /** Type of this claim.
    */
  protected type T

  /** Json writer for this claim's value.
    */
  protected def valueJsonWriter: JsonWriter[T]

  /** Name of this claim.
    *
    * @return claim name
    */
  def name: String

  /** Value of this claim.
    *
    * @return claim value
    */
  def value: T

  /** JSON representation of this claim's value.
    *
    * @return JSON value
    */
  private[sprayjwt] def valueAsJson: JsonValue = {
    valueJsonWriter.write(this.value)
  }
}

object Claim {

  /** The "exp" (expiration time) claim identifies the expiration time on or
    * after which the JWT MUST NOT be accepted for processing.
    *
    * @param value seconds since the epoch
    */
  final case class ExpirationTime(value: NumericDate) extends Claim {

    override type T = NumericDate

    override def valueJsonWriter: JsonWriter[T] = NumericDate.jsonWriter

    override def name: String = "exp"
  }

  /** Represents a private claim that producer and consumer of a JWT can agree
    * to use freely unlike registered or public claims.
    *
    * @param name name of this claim
    * @param value value of this claim
    */
  final case class Private[C: JsonWriter](
      name: String,
      value: C
  ) extends Claim {

    override type T = C

    override def valueJsonWriter: JsonWriter[T] = implicitly[JsonWriter[T]]
  }
}
