package io.neilljohnston.github.exmath

import com.badlogic.gdx.math.Rectangle

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Two grids, offset by half of their cell widths.
  * An object can belong to exactly two cells, one in each grid.
  * If an object is larger than a quarter of a cell, it will be split into 4 sub-objects, and placed in the grids.
  * Therefore, for optimization, a good cell size is four times the largest size of a typical object.
  *
  * Intersection checking is performed by going through each grid once, checking possible pairs.
  * To avoid multiple intersections per pair, a list of intersection pairs is maintained each time checking is
  * performed, then collision logic can be applied to each object in the pair.
  *
  * The entire spacial map is limited by gridWidth and gridHeight, and can be offset for checking different areas. Use
  * ...
  *
  * Worst case for complexity: O(2*n**2), I suppose. Probably never going to happen, but it REALLY sucks.
  * Best case for complexity: O(2*n), each object is checked exactly once and no intersections happen. I suppose.
  * Typical case for complexity: O(2*I'm not a computer scientist yet, I don't fucking know.) It might not be too bad.
  * I suppose.
  */
class SplitGridSpacialMap(cellWidth: Float, cellHeight: Float, val gridWidth: Int, val gridHeight: Int,
        var xOffset: Float = 0, var yOffset: Float = 0) {
    // Contains objects that will be checked and mapped. It's a hashset for obvious reasons.
    // TODO document the reasons that should be obvious.
    val objects: mutable.HashSet[Rectangle] = new mutable.HashSet[Rectangle]

    // Two space components, the "normal" one and the offset one.
    val space: Array[Array[mutable.HashSet[Rectangle]]] = Array.ofDim(gridHeight, gridWidth)
    val spaceOff: Array[Array[mutable.HashSet[Rectangle]]] = Array.ofDim(gridHeight, gridWidth)

    // Two additional structures to save the populated cell locations.
    val populatedSpace: mutable.HashSet[(Int, Int)] = new mutable.HashSet[(Int, Int)]
    val populatedSpaceOff: mutable.HashSet[(Int, Int)] = new mutable.HashSet[(Int, Int)]

    // How much the offset grid is offset by.
    val offCellWidth: Float = cellWidth / 2
    val offCellHeight: Float = cellHeight / 2

    /**
      * Add an object to the spacial map.
      * @param that The object to add
      */
    def add(that: Rectangle): Unit = objects += that

    /**
      * Add a bunch of objects to the spacial map.
      * @param those    The objects to add
      */
    def add(those: Traversable[Rectangle]): Unit = objects ++= those

    /**
      * Remove an object from the spacial map.
      * @param that The object to remove
      */
    def remove(that: Rectangle): Unit = objects -= that

    /**
      * Remove a bunch of objects from the spacial map.
      * @param those    The objects to remove
      */
    def remove(those: Traversable[Rectangle]): Unit = objects --= those

    /**
      * Update each object in the map.
      */
    def updateSpace(): Unit = {
        // Clear the hashsets at first
        for((x, y) <- populatedSpace) space(x)(y) = new mutable.HashSet[Rectangle]
        for((x, y) <- populatedSpaceOff) spaceOff(x)(y) = new mutable.HashSet[Rectangle]
        populatedSpace.clear()
        populatedSpaceOff.clear()
        // Add objects in their respective locations
        for(o <- objects) {
            val(x, y, xOff, yOff) = spaceLocation(o)
            space(x)(y) += o
            spaceOff(xOff)(yOff) += o
            populatedSpace += ((x, y))
            populatedSpaceOff += ((xOff, yOff))
        }
    }

    /**
      * Returns possibly the most egregious data structure I've ever made to represent a location.
      * @param that Rectangle whomst've'se location we need
      * @return Location in space, in the form of ((x, y), (xOff, yOff))
      */
    def spaceLocation(that: Rectangle): (Int, Int, Int, Int) = (
            (that.x / cellWidth - xOffset).toInt,
            (that.y / cellHeight - xOffset).toInt,
            ((that.x + offCellWidth) / cellWidth - xOffset).toInt,
            ((that.y + offCellHeight) / cellHeight - yOffset).toInt
    )

    /**
      * Find possible intersections with that.
      * @param that A rectangle to be tested for spacial region
      * @return All objects that could possibly intersect with that
      */
    def intersections(that: Rectangle): Set[Rectangle] = {
        val(x, y, xOff, yOff) = spaceLocation(that)
        val inSpace = (for(o <- space(x)(y)) yield o).toSet
        val inSpaceOff = (for(o <- space(xOff)(yOff)) yield o).toSet
        inSpace ++ inSpaceOff - that
    }

    /**
      * Reposition the offset of the space.
      * @param x    New x-offset
      * @param y    New y-offset
      */
    def offset(x: Float, y: Float): Unit = {
        xOffset = x
        yOffset = y
    }
}
