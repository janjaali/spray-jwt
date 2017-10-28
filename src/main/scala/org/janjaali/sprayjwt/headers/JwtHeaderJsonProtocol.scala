package org.janjaali.sprayjwt.headers

import org.janjaali.sprayjwt.algorithms.HashingAlgorithm
import spray.json.{DefaultJsonProtocol, DeserializationException, JsObject, JsString, JsValue, RootJsonFormat}

/**
  * Implicit converter for JWT-Headers to JSON.
  */
trait JwtHeaderJsonProtocol extends DefaultJsonProtocol {

  /**
    * Implicitly converts JWT-Header to JSON.
    */
  implicit object JwtHeaderJsonFormat extends RootJsonFormat[JwtHeader] {
    override def write(jwtHeader: JwtHeader): JsValue = {
      JsObject(
        "typ" -> JsString(jwtHeader.typ),
        "alg" -> JsString(jwtHeader.algorithm.name)
      )
    }

    override def read(json: JsValue): JwtHeader = {
      json.asJsObject.getFields("typ", "alg") match {
        case Seq(JsString(typ), JsString(alg)) if typ == "JWT" =>
          HashingAlgorithm(alg) match {
            case Some(algorithm) => JwtHeader(algorithm)
            case _ => throw DeserializationException("Unknown hashing algorithm")
          }
        case _ => throw DeserializationException("JwtHeader expected")
      }
    }
  }


}
