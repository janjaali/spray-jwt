package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.algorithms
import org.janjaali.sprayjwt.util.CollectionsFactory

/** Contains the parameter that describes the cryptographic operations and
  * parameters employed to JWS Protected Header's and a JWS Payload.
  *
  * @param protectedHeader set of contained protected Header
  * @param unprotectedHeader set of contained unprotected Header
  */
sealed abstract case class JoseHeader private (
    protectedHeaders: Set[JoseHeader.Header.ProtectedHeader],
    unprotectedHeaders: Set[JoseHeader.Header.UnprotectedHeader]
) {

  def headers: Set[JoseHeader.Header] = {
    protectedHeaders ++ unprotectedHeaders
  }
}

object JoseHeader {

  /** Represents a Header Parameter.
    */
  trait Header {

    /** Type of this header.
      */
    protected type T

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
  }

  /** Headers.
    */
  object Header {

    /** JWS protected Header.
      */
    trait ProtectedHeader extends Header

    /** Protected headers.
      */
    object ProtectedHeader {

      /** Identifies the cryptographic algorithm used to secure the JWS
        *
        * @param value
        */
      final case class Algorithm(
          value: algorithms.Algorithm
      ) extends ProtectedHeader {

        override def name: String = "alg"

        override type T = algorithms.Algorithm
      }

      /** Represents a private protected header that producer and consumer of a
        * JWT can agree to use freely unlike registered headers.
        *
        * @param name name of this header
        * @param value value of this header
        */
      case class Private[H](name: String, value: H) extends ProtectedHeader {
        override type T = H
      }
    }

    /** JWS unprotected Header.
      */
    trait UnprotectedHeader extends Header

    object UnprotectedHeader {

      /** Represents a private unprotected header that producer and consumer of
        * a JWT can agree to use freely unlike registered headers.
        *
        * @param name name of this header
        * @param value value of this header
        */
      case class Private[H](name: String, value: H) extends UnprotectedHeader {
        override type T = H
      }
    }
  }

  /** Constructs a Jose Header for a set of headers.
    *
    * When multiple headers share the same name the later one in the given list
    * of headers remains in the resulting headers list, hereby protected headers
    * benefit from a higher precedence than unprotected headers.
    *
    * @param headers set of headers that should be added
    * @return Jose Header containing both the protected and unprotected headers
    */
  def apply(
      headers: Seq[Header]
  ): JoseHeader = {
    val (protectedHeaders, unprotectedHeaders) = {
      headers.foldLeft(
        (
          List.empty[Header.ProtectedHeader],
          List.empty[Header.UnprotectedHeader]
        )
      ) { case ((protectedHeaders, unprotectedHeaders), header) =>
        header match {
          case header: Header.ProtectedHeader =>
            (protectedHeaders :+ header, unprotectedHeaders)
          case header: Header.UnprotectedHeader =>
            (protectedHeaders, unprotectedHeaders :+ header)
        }
      }
    }

    val protectedHeadersWithUniqueNames = {
      CollectionsFactory.uniqueElements(protectedHeaders)(_.name)
    }

    val protectedHeaderNames = {
      protectedHeadersWithUniqueNames.map(_.name)
    }

    val unprotectedHeadersWithUniqueNames = {
      unprotectedHeaders.foldRight(List.empty[Header.UnprotectedHeader]) {
        case (header, headers) =>
          val existsHeaderWithSameName = {
            (headers.map(_.name) ++ protectedHeaderNames).contains(header.name)
          }

          if (existsHeaderWithSameName) {
            headers
          } else {
            header :: headers
          }
      }
    }

    new JoseHeader(
      protectedHeaders = protectedHeadersWithUniqueNames.toSet,
      unprotectedHeaders = unprotectedHeadersWithUniqueNames.toSet
    ) {}
  }
}
