package org.janjaali.sprayjwt.sprayjson

import org.janjaali.sprayjwt.json._
import spray.json._

object SprayJsonStringDeserializer extends JsonStringDeserializer:

  override def deserialize(jsonText: String): JsonValue =
    asJsonValue(jsonText.parseJson)

  private def asJsonValue(jsValue: JsValue): JsonValue =
    jsValue match
      case JsObject(fields) =>
        JsonObject(
          fields.map { case (name, jsValue) =>
            name -> asJsonValue(jsValue)
          }
        )

      case JsArray(elements) =>
        ???

      case JsString(value) =>
        JsonString(value)

      case JsNumber(value) =>
        JsonNumber(value)

      case jsBoolean: JsBoolean => JsonBoolean(jsBoolean.value)

      case JsNull => JsonNull
