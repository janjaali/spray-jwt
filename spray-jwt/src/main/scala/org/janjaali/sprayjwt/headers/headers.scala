package org.janjaali.sprayjwt

import org.janjaali.sprayjwt.algorithms.Algorithm
import org.janjaali.sprayjwt.exceptions.{InvalidJwtAlgorithmException, InvalidJwtHeaderException}
import spray.json.{JsObject, JsString, JsValue, JsonReader, JsonWriter}
import org.janjaali.sprayjwt.algorithms.Algorithm.Rsa.Rs256
import org.janjaali.sprayjwt.algorithms.Algorithm.Rsa.Rs384
import org.janjaali.sprayjwt.algorithms.Algorithm.Rsa.Rs512
import org.janjaali.sprayjwt.algorithms.Algorithm.Hmac.Hs256
import org.janjaali.sprayjwt.algorithms.Algorithm.Hmac.Hs384
import org.janjaali.sprayjwt.algorithms.Algorithm.Hmac.Hs512

/**
  * Package object for headers package.
  */
package object headers {

  /**
    * Implicit JsonWriter to write JWT header as JsValues.
    */
  implicit object JwtHeaderJsonWriter extends JsonWriter[JwtHeader] {
    def write(jwtHeader: JwtHeader): JsValue = {

      // TODO: Extract hardcoded strings out
      val algorithmIdentifier = jwtHeader.algorithm match {
        case Rs256 => "RS256"
        case Rs384 => "RS384"
        case Rs512 => "RS512"
        case Hs256 => "HS256"
        case Hs384 => "HS384"
        case Hs512 => "HS512"
      }


      JsObject(
        "alg" -> JsString(algorithmIdentifier),
        "typ" -> JsString(jwtHeader.typ)
      )
    }
  }

  /**
    * Implicit JsonReader to read JsValues as JWT header.
    */
  implicit object JwtHeaderJsonReader extends JsonReader[JwtHeader] {
    override def read(json: JsValue): JwtHeader = {
      json.asJsObject.getFields("alg", "typ") match {
        case Seq(JsString(alg), JsString(typ)) if typ == "JWT" =>

          val maybeAlgorithm: Option[Algorithm] = alg match {
            case "HS256" => Some(Algorithm.Hmac.Hs256)
            case "HS384" => Some(Algorithm.Hmac.Hs384)
            case "HS512" => Some(Algorithm.Hmac.Hs512)
            case "RS256" => Some(Algorithm.Rsa.Rs256)
            case "RS384" => Some(Algorithm.Rsa.Rs384)
            case "RS512" => Some(Algorithm.Rsa.Rs512)
          }

          maybeAlgorithm match {
            case Some(algorithm) => JwtHeader(algorithm)
            case _ => throw new InvalidJwtAlgorithmException(s"Unsupported JWT algorithm $alg")
          }
        case _ => throw new InvalidJwtHeaderException(s"Invalid jwt-header $json")
      }
    }
  }
}
