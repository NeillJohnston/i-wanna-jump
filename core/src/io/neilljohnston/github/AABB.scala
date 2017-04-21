package io.neilljohnston.github

import com.badlogic.gdx.math.Rectangle
import io.neilljohnston.github.IWannaJump.Ps

import scala.reflect.ClassTag

/**
  * A simple axis-aligned bounding box (AABB).
  */
class AABB extends Rectangle {}

/**
  * AABB companion object, with the actually useful bits. The AABB class is just an extension of Rectangle.
  */
object AABB {
    /**
      * An AABB with only top collisions.
      */
    class PlatformAABB extends AABB {}

    /**
      * An AABB just defined as a straight line.
      * @param m    Slope of the slope
      * @param b    Proportional y-intercept (b=0.75 means that the y-intercept is at three-quarters up the left side)
      */
    class SlopeAABB(val m: Float, b: Float) extends AABB {
        /**
          * The function of the slope's line.
          * @param $x   Line argument
          * @return The height of y at x for the line
          */
        def f($x: Float): Float = m * ($x - x) + (y + height * b)

        /**
          * Override overlaps to use the slope.
          * @param r    Rectangle to test overlapping
          * @return True if overlapping, false otherwise
          */
        override def overlaps(r: Rectangle): Boolean = super.overlaps(r) &&
                ((r.y < f(r.x) && f(r.x) < r.y + r.height) ||
                        (r.y < f(r.x + r.width) && f(r.x + r.width) < r.y + r.height))
    }

    /**
      * Constructs a new AABB of type T.
      * @param x    X-coordinate of AABB
      * @param y    Y-coordinate of AABB
      * @param aabb String representing AABB type
      * @return The new AABB
      */
    def factory(x: Float, y: Float, aabb: String): AABB = {
        val t = aabb match {
            case "platform" => new PlatformAABB
            case "slope.1" => new SlopeAABB(1, 0)
            case "slope.-1" => new SlopeAABB(-1, 1)
            case "slope.0.5,0" => new SlopeAABB(0.5f, 0)
            case "slope.0.5,0.5" => new SlopeAABB(0.5f, 0.5f)
            case "slope.-0.5,1" => new SlopeAABB(-0.5f, 1)
            case "slope.-0.5,0.5" => new SlopeAABB(-0.5f, 0.5f)
            case _ => new AABB
        }
        t.x = x
        t.y = y
        t.width = Ps
        t.height = Ps
        t
    }

    /**
      * Constructs a new AABB of type T.
      * @param x        X-coordinate of AABB
      * @param y        Y-coordinate of AABB
      * @param width    Width of AABB
      * @param height   Height of AABB
      * @param aabb String representing AABB type
      * @return The new AABB
      */
    def factory(x: Float, y: Float, width: Float, height: Float, aabb: String): AABB = {
        val t = factory(x, y, aabb)
        t.width = width
        t.height = height
        t
    }
}
