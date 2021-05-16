package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.json.*
import spray.json.*

/** spray-json implementation of the JsonStringSerializer.
  */
object SprayJsonStringSerializer extends JsonStringSerializer:

  override def serialize(jsonValue: JsonValue): String =
    asJsValue(jsonValue).compactPrint

  private def asJsValue(jsonValue: JsonValue): JsValue =
    jsonValue match
      case JsonObject(members) =>
        JsObject(
          members.map { case (key, value) =>
            key -> asJsValue(value)
          }.toMap
        )

      case JsonArray(elements) =>
        JsArray(
          elements.map(asJsValue).toVector
        )

      case JsonString(value)  => JsString(value)
      case JsonNumber(value)  => JsNumber(value)
      case JsonBoolean(value) => JsBoolean(value)
      case JsonNull           => JsNull
