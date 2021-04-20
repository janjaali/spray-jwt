package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.json._
import spray.json._

object SprayJsonStringSerializer extends JsonStringSerializer {

  override def serialize(jsonValue: JsonValue): String = {
    sprayJsonValue(jsonValue).compactPrint
  }

  private def sprayJsonValue(jsonValue: JsonValue): JsValue = {
    jsonValue match {
      case JsonObject(members) =>
        JsObject(
          members.map { case (key, value) =>
            key -> sprayJsonValue(value)
          }.toMap
        )
      case JsonString(value) =>
        JsString(value)
      case JsonNumber(value) =>
        JsNumber(value)
      case JsonBoolean(value) =>
        JsBoolean(value)
    }
  }
}
