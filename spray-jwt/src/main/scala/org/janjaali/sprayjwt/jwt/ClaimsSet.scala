package org.janjaali.sprayjwt.jwt

import org.janjaali.sprayjwt.util.CollectionsFactory

/** Claims Set represents a data structure whose members are the claims. The
  * claim names within a Claims Set MUST be unique.
  *
  * @param claims uniquely named claims
  */
sealed abstract case class ClaimsSet private (claims: Set[Claim])

object ClaimsSet {

  /** Constructs a Claims Set for a set of uniquely named claims.
    *
    * When multiple claims share the same name the later one in the given list
    * of claims remains in the resulting claims set.
    *
    * @param claims claims that should be added to the Claims Set.
    * @return Claims Set
    */
  def apply(claims: Seq[Claim]): ClaimsSet = {

    val claimsWithUniqueNames = {
      CollectionsFactory.uniqueElements(claims)(_.name)
    }

    new ClaimsSet(claimsWithUniqueNames.toSet) {}
  }
}
