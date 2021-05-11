package org.janjaali.sprayjwt.json

/** Represents a JSON value.
  */
sealed trait JsonValue

/** Represents a JSON object consisting of a set of members (i.e. name -> JSON
  * value pairs).
  *
  * @param members set of name -> JSON value pairs
  */
final case class JsonObject(members: Map[String, JsonValue]) extends JsonValue

/** Represents a JSON array consisting of a set of elements (i.e. JSON values).
  * 
  * @param elements set of JSON values 
  */
final case class JsonArray(elements: Seq[JsonValue]) extends JsonValue

/** Represents a JSON string.
  * 
  * @param value string value
  */
final case class JsonString(value: String) extends JsonValue

/** Represents a JSON number.
  *
  * @param value number value 
  */
final case class JsonNumber(value: BigDecimal) extends JsonValue

/** Represents a JSON boolean.
  * 
  * @param value boolean value 
  */
final case class JsonBoolean(value: Boolean) extends JsonValue

/** Represents a JSON null value. 
  */
case object JsonNull extends JsonValue
