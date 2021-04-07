package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.algorithms
import org.janjaali.sprayjwt.json.{JsonString, JsonValue, JsonWriter}

/** Represents a Header.
  */
trait Header {

  /** Type of this header.
    */
  protected type T

  /** Json writer for this header's value.
    */
  protected def valueJsonWriter: JsonWriter[T]

  /** Name fo this header.
    *
    * @return header name
    */
  def name: String

  /** Value of this header.
    *
    * @return header value
    */
  def value: T

  /** JSON representation of this header's value.
    *
    * @return JSON value
    */
  private[sprayjwt] def valueAsJson: JsonValue = {
    valueJsonWriter.write(this.value)
  }
}

/** Provide headers.
  */
object Header {

  /** Identifies the cryptographic algorithm used to secure the JWS
    *
    * @param value algorithm
    */
  final case class Algorithm(
      value: algorithms.Algorithm
  ) extends Header {

    override def name: String = "alg"

    override type T = algorithms.Algorithm

    override protected def valueJsonWriter: JsonWriter[
      algorithms.Algorithm
    ] = {
      new JsonWriter[algorithms.Algorithm] {

        override def write(algorithm: algorithms.Algorithm): JsonValue = {
          algorithm match {
            case algorithms.Algorithm.Rsa.Rs256  => JsonString("RS256")
            case algorithms.Algorithm.Rsa.Rs384  => JsonString("RS384")
            case algorithms.Algorithm.Rsa.Rs512  => JsonString("RS512")
            case algorithms.Algorithm.Hmac.Hs256 => JsonString("HS256")
            case algorithms.Algorithm.Hmac.Hs384 => JsonString("HS384")
            case algorithms.Algorithm.Hmac.Hs512 => JsonString("HS512")
          }
        }
      }
    }
  }

  /** Declares the media type of the complete JWS.
    *
    *  @param value type
    */
  final case class Type(
      value: Type.Value
  ) extends Header {

    override def name: String = "typ"

    override type T = Type.Value

    override protected def valueJsonWriter: JsonWriter[Type.Value] = {
      new JsonWriter[Type.Value] {

        override def write(value: Type.Value): JsonValue = {
          value match {
            case Type.Value.Jwt => JsonString("JWT")
          }
        }
      }
    }
  }

  /** Provides type values.
    */
  object Type {

    sealed trait Value

    object Value {

      /** Type value JWT.
        */
      case object Jwt extends Value
    }
  }

  /** Represents a private header that producer and consumer of a JWT can
    * agree to use freely unlike registered headers.
    *
    * @param name name of this header
    * @param value value of this header
    */
  case class Private[H: JsonWriter](name: String, value: H) extends Header {
    override type T = H

    override protected def valueJsonWriter: JsonWriter[H] = {
      implicitly[JsonWriter[T]]
    }
  }
}
