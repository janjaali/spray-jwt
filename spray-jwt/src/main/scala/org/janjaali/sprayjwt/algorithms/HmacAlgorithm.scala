package org.janjaali.sprayjwt.algorithms

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import org.janjaali.sprayjwt.encoder.{Base64Encoder, ByteEncoder}

/** Hash-based Message Authentication Codes (HMACs) algorithm to sign and
  * validate digital signatures.
  */
sealed trait HmacAlgorithm extends Algorithm {

  private val provider = "SunJCE"

  protected def hashingAlgorithmName: String

  // TODO: Check implementation
  override def sign(data: String, secret: String): String = {

    val secretAsByteArray = ByteEncoder.getBytes(secret)
    val secretKey = new SecretKeySpec(secretAsByteArray, hashingAlgorithmName)

    val dataAsByteArray = ByteEncoder.getBytes(data)

    val mac = Mac.getInstance(hashingAlgorithmName, provider)
    mac.init(secretKey)
    val signAsByteArray = mac.doFinal(dataAsByteArray)
    Base64Encoder.encode(signAsByteArray)
  }

  // TODO: Check implementation
  override def validate(
      signature: String,
      data: String,
      secret: String
  ): Boolean = {
    sign(data, secret) == signature
  }
}

object HmacAlgorithm {

  case object Hs256 extends HmacAlgorithm {

    override val name: String = "HS256"

    override val hashingAlgorithmName = "HMACSHA256"
  }

  case object Hs384 extends HmacAlgorithm {

    override val name: String = "HS384"

    override val hashingAlgorithmName = "HMACSHA384"
  }

  case object Hs512 extends HmacAlgorithm {

    override val name: String = "HS512"

    override val hashingAlgorithmName = "HMACSHA512"
  }
}
