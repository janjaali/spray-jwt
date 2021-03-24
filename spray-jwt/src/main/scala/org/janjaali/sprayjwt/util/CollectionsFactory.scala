package org.janjaali.sprayjwt.util

/** Utilities methods to setup collections with given properties.
  */
object CollectionsFactory {

  /** Creates a collection from a given collecting by ensuring that the given
    * predicate uniquely matches for at most one element of the returned
    * collection.
    *
    * When multiple elements match the same predicate the later one in the
    * sequence remains in the resulting sequence.
    *
    * <b>Example</b>:
    *
    * <pre>
    * val origin = List(("a", 1), ("b", 2), ("c", 3), ("b", 4), ("a", 5))
    * val result = uniqueElements(origin) { elem => elem._1 }
    *
    * result // => List((c,3), (b,4), (a,5))
    * </pre>
    *
    * @param collection
    * @param predicate
    * @return sequence containing at most one element that matches the predicate
    */
  def uniqueElements[T, U](collection: Seq[T])(predicate: T => U): Seq[T] = {
    collection.foldRight(List.empty[T]) { case (elem, collection) =>
      if (collection.map(predicate).contains(predicate(elem))) {
        collection
      } else {
        elem :: collection
      }
    }
  }
}
