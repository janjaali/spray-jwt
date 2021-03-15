package org.janjaali.sprayjwt.jwt.model

import org.janjaali.sprayjwt.json.JsonObject

/** JWS Payload consisting of a Claims Set.
  *
  * @param claims claims of this JWS Payload
  */
final case class JwsPayload(claims: ClaimsSet) {

  /** JSON representation of this JWS payload.
    *
    * @return JSON object
    */
  def asJson: JsonObject = {
    JsonObject(
      claims.claims.map { claim =>
        claim.name -> claim.valueAsJson
      }.toMap
    )
  }
}
