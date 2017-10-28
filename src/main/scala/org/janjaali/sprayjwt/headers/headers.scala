package org.janjaali.sprayjwt

import spray.json.{JsObject, JsString, JsValue, JsonWriter}

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

}
