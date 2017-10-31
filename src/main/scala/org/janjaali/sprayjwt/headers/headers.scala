package org.janjaali.sprayjwt

import org.janjaali.sprayjwt.algorithms.{HS256, HashingAlgorithm}
import org.janjaali.sprayjwt.exceptions.{InvalidJwtAlgorithmException, InvalidJwtHeaderException}
import spray.json.{JsObject, JsString, JsValue, JsonReader, JsonWriter}

package object headers {

  /**
    * Implicit JsonWriter to write JWT-Header as JSON.
    */
  implicit object JwtHeaderJsonWriter extends JsonWriter[JwtHeader] {
    def write(jwtHeader: JwtHeader): JsValue = {
      JsObject(
        "alg" -> JsString(jwtHeader.algorithm.name),
        "typ" -> JsString(jwtHeader.typ)
      )
    }
  }

  implicit object JwtHeaderJsonReader extends JsonReader[JwtHeader] {
    override def read(json: JsValue): JwtHeader = {
      json.asJsObject.getFields("alg", "typ") match {
        case Seq(JsString(alg), JsString(typ)) if typ == "JWT" =>
          HashingAlgorithm(alg) match {
            case Some(algorithm) => JwtHeader(algorithm)
            case _ => throw new InvalidJwtAlgorithmException(s"Unsupported JWT algorithm $alg")
          }
        case _ => throw new InvalidJwtHeaderException(s"Invalid jwt-header $json")
      }
    }
  }

}
