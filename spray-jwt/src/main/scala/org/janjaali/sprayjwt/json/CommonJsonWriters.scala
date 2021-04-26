package org.janjaali.sprayjwt.json

/** Commonly used JSON writers for commonly used types.
  */
object CommonJsonWriters {

  /** Implicitly usable CommonJsonWriters.
    */
  object Implicits {

    /** Constructs JSON writer for Int types.
      *
      * @return JSON writer
      */
    implicit def intJsonWriter: JsonWriter[Int] = {
      CommonJsonWriters.intJsonWriter
    }

    /** Constructs JSON writer for Long types.
      *
      * @return JSON writer
      */
    implicit def longJsonWriter: JsonWriter[Long] = {
      CommonJsonWriters.longJsonWriter
    }

    /** Constructs JSON writer for Boolean types.
      *
      * @return JSON writer
      */
    implicit def booleanJsonWriter: JsonWriter[Boolean] = {
      CommonJsonWriters.booleanJsonWriter
    }

    /** Constructs JSON writer for String types.
      *
      * @return JSON writer
      */
    implicit def stringJsonWriter: JsonWriter[String] = {
      CommonJsonWriters.stringJsonWriter
    }

    /** Constructs JSON writer for JSON values.
      *
      * @return JSON writer
      */
    implicit def jsonValueJsonWriter: JsonWriter[JsonValue] = {
      CommonJsonWriters.jsonValueJsonWriter
    }
  }

  /** Constructs JSON writer for Int types.
    *
    * @return JSON writer
    */
  def intJsonWriter: JsonWriter[Int] = {
    new JsonWriter[Int] {
      override def write(value: Int): JsonValue = {
        JsonNumber(BigDecimal(value))
      }
    }
  }

  /** Constructs JSON writer for Long types.
    *
    * @return JSON writer
    */
  def longJsonWriter: JsonWriter[Long] = {
    new JsonWriter[Long] {
      override def write(value: Long): JsonValue = {
        JsonNumber(value)
      }
    }
  }

  /** Constructs JSON writer for Boolean types.
    *
    * @return JSON writer
    */
  def booleanJsonWriter: JsonWriter[Boolean] = {
    new JsonWriter[Boolean] {
      override def write(value: Boolean): JsonValue = {
        JsonBoolean(value)
      }
    }
  }

  /** Constructs JSON writer for String types.
    *
    * @return JSON writer
    */
  def stringJsonWriter: JsonWriter[String] = {
    new JsonWriter[String] {
      override def write(value: String): JsonValue = {
        JsonString(value)
      }
    }
  }

  /** Constructs flat JSON writer for product types with at least an arity of 1.
    *
    * Ignores all other elements for JSON writing than the first element.
    *
    *  @return JSON writer
    */
  def flatValueJsonWriter[P <: Product, T: JsonWriter]: JsonWriter[P] = {
    new JsonWriter[P] {
      def write(product: P): JsonValue = {
        val valueWriter = implicitly[JsonWriter[T]]
        valueWriter.write(product.productElement(0).asInstanceOf[T])
      }
    }
  }

  /** Constructs JSON writer for JSON values.
    *
    * @return JSON writer
    */
  def jsonValueJsonWriter = {
    new JsonWriter[JsonValue] {
      override def write(value: JsonValue): JsonValue = value
    }
  }
}
