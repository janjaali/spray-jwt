package org.janjaali.sprayjwt.json

sealed trait JsonValue

case class JsonObject(members: Map[String, JsonValue]) extends JsonValue

final case class JsonString(value: String) extends JsonValue

final case class JsonNumber[T: Numeric](value: T) extends JsonValue
