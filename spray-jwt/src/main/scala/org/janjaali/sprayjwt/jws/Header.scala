package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.algorithms
import org.janjaali.sprayjwt.json._

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

    override def name: String = Algorithm.name

    override type T = algorithms.Algorithm

    override protected def valueJsonWriter: JsonWriter[algorithms.Algorithm] = {
      Algorithm.valueJsonWriter
    }
  }

  object Algorithm {

    val name: String = "alg"

    private def valueJsonWriter: JsonWriter[algorithms.Algorithm] = {
      new JsonWriter[algorithms.Algorithm] {
        override def write(algorithm: algorithms.Algorithm): JsonValue =
          algorithm match
            case algorithms.Algorithm.Rsa.Rs256  => JsonString("RS256")
            case algorithms.Algorithm.Rsa.Rs384  => JsonString("RS384")
            case algorithms.Algorithm.Rsa.Rs512  => JsonString("RS512")
            case algorithms.Algorithm.Hmac.Hs256 => JsonString("HS256")
            case algorithms.Algorithm.Hmac.Hs384 => JsonString("HS384")
            case algorithms.Algorithm.Hmac.Hs512 => JsonString("HS512")

      }
    }

    private[Header] def apply(algorithmName: String): Option[Algorithm] =
      algorithmName match
        case "RS256" => Some(Algorithm(algorithms.Algorithm.Rsa.Rs256))
        case "RS384" => Some(Algorithm(algorithms.Algorithm.Rsa.Rs384))
        case "RS512" => Some(Algorithm(algorithms.Algorithm.Rsa.Rs512))
        case "HS256" => Some(Algorithm(algorithms.Algorithm.Hmac.Hs256))
        case "HS384" => Some(Algorithm(algorithms.Algorithm.Hmac.Hs384))
        case "HS512" => Some(Algorithm(algorithms.Algorithm.Hmac.Hs512))
        case _       => None
  }

  /** Declares the media type of the complete JWS.
    *
    *  @param value type
    */
  final case class Type(
      value: Type.Value
  ) extends Header {

    override def name: String = Type.name

    override type T = Type.Value

    override protected def valueJsonWriter: JsonWriter[Type.Value] = {
      Type.valueJsonWriter
    }
  }

  /** Provides type values.
    */
  object Type {

    val name: String = "typ"

    sealed trait Value

    object Value {

      /** Type value JWT.
        */
      case object Jwt extends Value
    }

    private def valueJsonWriter: JsonWriter[Type.Value] = {
      new JsonWriter[Type.Value] {
        override def write(value: Type.Value): JsonValue = {
          value match {
            case Type.Value.Jwt => JsonString("JWT")
          }
        }
      }
    }

    private[Header] def apply(typeValue: String): Option[Type] = {
      typeValue match {
        case "JWT" => Some(Type(Type.Value.Jwt))
        case _     => None
      }
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

  // TODO: Add docs.

  def apply(name: String, value: JsonValue): Header = {

    import CommonJsonWriters.Implicits.jsonValueJsonWriter

    (name, value) match {
      case (Algorithm.name, JsonString(algorithmName))
          if Algorithm(algorithmName).isDefined =>
        Algorithm(algorithmName).get
      case (Type.name, JsonString(typeValue)) if Type(typeValue).isDefined =>
        Type(typeValue).get
      case (name, value) =>
        Private(name, value)
    }
  }
}
