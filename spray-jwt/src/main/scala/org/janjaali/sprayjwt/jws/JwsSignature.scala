package org.janjaali.sprayjwt.jws

/** Digital signature or MAC over the JWS Protected Header and the JWS Payload.
  */
final case class JwsSignature(value: String) extends AnyVal
