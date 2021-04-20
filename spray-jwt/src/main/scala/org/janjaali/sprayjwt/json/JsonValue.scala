package org.janjaali.sprayjwt.json

// TODO: Check accessibility.
// TODO: Add docs.

sealed trait JsonValue

final case class JsonObject(members: Map[String, JsonValue]) extends JsonValue

final case class JsonString(value: String) extends JsonValue

final case class JsonNumber(value: BigDecimal) extends JsonValue

final case class JsonBoolean(value: Boolean) extends JsonValue
