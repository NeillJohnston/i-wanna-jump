package io.github.neilljohnston.iwannajump.java.elements;

import com.badlogic.gdx.math.Rectangle;

/**
 * A simple axis-aligned bound box (AABB).
 *
 * @author Neill Johnston
 */
public class AABB extends Rectangle {
    /**
     * Construct an AABB from given parameters.
     *
     * @param x         X-coordinate
     * @param y         Y-coordinate
     * @param width     Width
     * @param height    Height
     */
    public AABB(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Construct an AABB from another rectangle.
     *
     * @param rectangle The rectangle to copy from
     */
    public AABB(Rectangle rectangle) {
        this.x = rectangle.x;
        this.y = rectangle.y;
        this.width = rectangle.width;
        this.height = rectangle.height;
    }

    /**
     * Default constructor, does nothing.
     */
    public AABB() {
        //
    }

    /**
     * Report the overarching type of this AABB.
     *
     * @return  The hierarchically greatest class that this AABB should represent
     */
    public Class<? extends AABB> type() {
        return AABB.class;
    }
}
