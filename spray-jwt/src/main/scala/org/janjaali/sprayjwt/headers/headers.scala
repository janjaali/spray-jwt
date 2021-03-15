package org.janjaali.sprayjwt

import org.janjaali.sprayjwt.algorithms.Algorithm
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
      JsObject(
        "alg" -> JsString(jwtHeader.algorithm.name),
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
          Algorithm.forName(alg) match {
            case Some(algorithm) => JwtHeader(algorithm)
            case _ => throw new InvalidJwtAlgorithmException(s"Unsupported JWT algorithm $alg")
          }
        case _ => throw new InvalidJwtHeaderException(s"Invalid jwt-header $json")
      }
    }
  }
} // TODO: Many test are probably failing, Fix them!
