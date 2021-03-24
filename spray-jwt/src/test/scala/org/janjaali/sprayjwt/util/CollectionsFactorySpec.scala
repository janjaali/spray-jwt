package org.janjaali.sprayjwt.util

import org.janjaali.sprayjwt.tests.ScalaTestSpec

class CollectionsFactorySpec extends ScalaTestSpec {

  "CollectionsFactory" - {

    val sut = CollectionsFactory

    "should create collections containing at most one element for a given predicate" - {

      "when empty should be empty" in {

        sut.uniqueElements(Nil)(_ => true) shouldBe Nil
      }

      "when predicate matches all should contain last matching element." in {

        sut.uniqueElements(List(1, 2, 3))(_ < 10) shouldBe List(3)
      }

      "when predicate matches none should contain last none matching element." in {

        sut.uniqueElements(List(1, 2, 3))(_ > 10) shouldBe List(3)
      }

      "when predicate matches multiple elements should contain last matching element." in {

        val pairs = List(("a", 1), ("b", 2), ("c", 3), ("b", 4), ("a", 5))

        sut.uniqueElements(pairs)(elem => elem._1) shouldBe {
          List(("c", 3), ("b", 4), ("a", 5))
        }
      }
    }
  }
}
