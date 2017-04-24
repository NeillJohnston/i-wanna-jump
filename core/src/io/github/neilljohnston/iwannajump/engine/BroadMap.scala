package io.github.neilljohnston.iwannajump.engine

/**
  * General form for a broad-phase map, which is intended for collision detection.
  * @tparam T   The type that will be mapped
  */
trait BroadMap[T] extends Iterable[T] {
    /**
      * Adds a T object to the map.
      * @param that Object to add
      */
    def add(that: T): Unit

    /**
      * Removes a T object from the map.
      * @param that Object to remove
      */
    def remove(that: T): Unit

    /**
      * Scans for possible collisions with a T object and returns them in a set.
      * @param that Object to check
      * @return Possible (unchecked) intersections with that
      */
    def scan(that: T): Set[T]

    /**
      * Updates the map.
      */
    def update(): Unit
}
