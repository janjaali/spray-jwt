package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.util.CollectionsFactory
import org.janjaali.sprayjwt.json.JsonObject

/** Javascript Object Signing and Encryption (JOSE Header) that contains
  * the parameter that describes the cryptographic operations and
  * parameters employed to JWS Protected Header's and a JWS Payload.
  *
  * @param headers set of contained headers
  */
sealed abstract case class JoseHeader private (headers: Set[Header]) {

  def asJson: JsonObject = {
    JsonObject(
      headers.map { header =>
        header.name -> header.valueAsJson
      }.toMap
    )
  }
}

object JoseHeader {

  /** Constructs a JOSE Header for a sequence of headers.
    *
    * When multiple headers share the same name the later one in the given list
    * of headers remains in the resulting headers list.
    *
    * @param headers set of headers that should be added
    * @return JOSE Header
    */
  def apply(
      headers: Seq[Header]
  ): JoseHeader = {

    val uniquelyNamedHeaders = {
      CollectionsFactory.uniqueElements(headers)(_.name)
    }

    new JoseHeader(uniquelyNamedHeaders.toSet) {}
  }
}
