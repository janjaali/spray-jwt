package org.janjaali.sprayjwt.jws

import org.janjaali.sprayjwt.json.JsonObject
import org.janjaali.sprayjwt.jwt.JwtClaimsSet

/** Payload that consists of the Claims Set that has to be secured.
  *
  * @param claimsSet claims set that has to be secured
  */
final case class JwsPayload(claimsSet: JwtClaimsSet) {

  /** JSON representation of this JWS Payload.
    *
    * @return JSON object
    */
  def asJson: JsonObject = {
    JsonObject(
      claimsSet.claims.map { claim =>
        claim.name -> claim.valueAsJson
      }.toMap
    )
  }
}
