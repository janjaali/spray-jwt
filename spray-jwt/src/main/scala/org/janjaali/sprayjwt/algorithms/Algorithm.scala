package org.janjaali.sprayjwt.algorithms

/** Represents a cryptographic algorithms and identifiers used with JWT.
  */
private[sprayjwt] trait Algorithm {

  /** Name of this algorithm.
    *
    * @return name
    */
  def name: String

  // TODO: Maybe a better suited signature
  // Digitally signs the contents of the given JWS Header and the JWS Payload.
  // def sign(header: JoseHeader, payload: JwsPayload): String

  /** Signs data. // TODO: legacy?
    *
    * @param data   the data to sign
    * @param secret the secret to use for signing the data
    * @return signed data
    */
  def sign(data: String, secret: String): String

  /** Validates signature. // TODO: legacy?
    *
    * @param signature the signature to validate
    * @param data      the data to validate signature for
    * @param secret    the secret to use for validation
    * @return <code>true</code> if signature is valid, otherwise returns <code>false</code>
    */
  def validate(signature: String, data: String, secret: String): Boolean
}

/** Companion object to map Strings as hashing algorithms.
  */
object Algorithm {

  private val all = {
    List(
      HmacAlgorithm.Hs256,
      HmacAlgorithm.Hs384,
      HmacAlgorithm.Hs512,
      RsaAlgorithm.Rs256,
      RsaAlgorithm.Rs384,
      RsaAlgorithm.Rs512
    )
  }

  /** Resolves a given algorithm name to an algorithm implementation.
    *
    * @param name name of algorithm
    * @return algorithm
    */
  def forName(name: String): Option[Algorithm] = {
    Algorithm.all.find(_.name == name)
  }
}
