package org.janjaali.sprayjwt

import org.janjaali.sprayjwt.algorithms.{Algorithm, Algorithms}
import org.janjaali.sprayjwt.exceptions.{InvalidJwtAlgorithmException, InvalidJwtHeaderException}
import spray.json.{JsObject, JsString, JsValue, JsonReader, JsonWriter}

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
        case Algorithms.Rs256 => "RS256"
        case Algorithms.Rs384 => "RS384"
        case Algorithms.Rs512 => "RS512"
        case Algorithms.Hs256 => "HS256"
        case Algorithms.Hs384 => "HS384"
        case Algorithms.Hs512 => "HS512"
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
            case "HS256" => Some(Algorithms.Hs256)
            case "HS384" => Some(Algorithms.Hs384)
            case "HS512" => Some(Algorithms.Hs512)
            case "RS256" => Some(Algorithms.Rs256)
            case "RS384" => Some(Algorithms.Rs384)
            case "RS512" => Some(Algorithms.Rs512)
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
