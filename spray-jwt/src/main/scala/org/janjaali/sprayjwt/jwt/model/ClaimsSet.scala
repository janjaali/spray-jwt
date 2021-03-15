package org.janjaali.sprayjwt.jwt.model

/** Claims Set represents a data structure whose members are the claims. The
  * claim names within a Claims Set MUST be unique.
  *
  * @param claims uniquely named claims
  */
sealed abstract case class ClaimsSet private (claims: Set[Claim])

object ClaimsSet {

  /** Constructs a Claim Set for a set of uniquely named claims.
    *
    * Claims with duplicated claim names override pre-existing claims in the
    * same set.
    *
    * @param claims uniquely named claims
    * @return Claim Set
    */
  def apply(claims: Seq[Claim]): ClaimsSet = {

    val claimsWithUniqueNames = {
      claims.foldRight(List.empty[Claim]) { case (claim, claims) =>
        if (claims.map(_.name).contains(claim.name)) {
          claims
        } else {
          claim :: claims
        }
      }
    }

    new ClaimsSet(claimsWithUniqueNames.toSet) {}
  }
}
