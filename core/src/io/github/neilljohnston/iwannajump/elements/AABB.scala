package io.github.neilljohnston.iwannajump.elements

import com.badlogic.gdx.math.Rectangle
import io.github.neilljohnston.iwannajump.engine.IWannaJump.Ps

/**
  * A simple axis-aligned bounding box (AABB).
  */
class AABB extends Rectangle {
    def constructFrom(that: Rectangle): AABB = {
        x = that.x
        y = that.y
        width = that.width
        height = that.height
        this
    }
}

/**
  * AABB companion object, with the actually useful bits. The AABB class is just an extension of Rectangle.
  */
object AABB {
    /**
      * An AABB that contains some functionality for implementing friction force.
      * @param mu   Amount of friction
      */
    class FrictionAABB(val mu: Float) extends AABB {}

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
      * The AABB when you don't want an AABB!
      * Used for assigning cosmetics in the tiles layer of a map.
      */
    class NoneAABB extends AABB {
        x = Float.MinValue
        y = 0
        width = 0
        height = 0
    }

    /**
      * Constructs a new AABB of type T.
      * @param x    X-coordinate of AABB
      * @param y    Y-coordinate of AABB
      * @param aabb String representing AABB type
      * @return The new AABB
      */
    def factory(x: Float, y: Float, aabb: String): AABB = {
        val from = new Rectangle(x, y, Ps, Ps)
        aabb match {
            case "platform" => new PlatformAABB().constructFrom(from)
            case "cosmetic" => new NoneAABB
            case "slope.1" => new SlopeAABB(1, 0).constructFrom(from)
            case "slope.-1" => new SlopeAABB(-1, 1).constructFrom(from)
            case "slope.0.5,0" => new SlopeAABB(0.5f, 0).constructFrom(from)
            case "slope.0.5,0.5" => new SlopeAABB(0.5f, 0.5f).constructFrom(from)
            case "slope.-0.5,1" => new SlopeAABB(-0.5f, 1).constructFrom(from)
            case "slope.-0.5,0.5" => new SlopeAABB(-0.5f, 0.5f).constructFrom(from)
            case _ => new AABB().constructFrom(from)
        }
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
        factory(x, y, aabb).constructFrom(new Rectangle(x, y, width, height))
    }
}
