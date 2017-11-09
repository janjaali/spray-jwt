package org.janjaali.sprayjwt

case class JwtClaims
(
  iss: Option[String] = None,
  sub: Option[String] = None,
  aud: Option[Set[String]] = None,
  exp: Option[Long] = None,
  nbf: Option[Long] = None,
  isa: Option[Long] = None,
  iat: Option[Long] = None,
  jti: Option[String] = None,
)