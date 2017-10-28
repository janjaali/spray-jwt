package org.janjaali.sprayjwt.algorithms

/**
  * Companion Object to map Strings to HashingAlgorithms.
  */
object HashingAlgorithm {
  def apply(name: String): Option[HashingAlgorithm] = name match {
    case "HS256" => Some(HS256)
    case _ => None
  }
}

/**
  * Abstract class for HashingAlgorithm.
  *
  * @param name of HashingAlgorithm
  */
sealed abstract class HashingAlgorithm(val name: String)

/**
  * Represents HS256 HashingAlgorithm.
  */
case object HS256 extends HashingAlgorithm("HS256")