package io.github.neilljohnston.iwannajump.java.elements;

import com.badlogic.gdx.math.Rectangle;

/**
 * A kind of AABB that has a mathematically defined slope on it, specified by a y-intercept and slope.
 */
public class SlopeAABB extends AABB {
    private final float m;
    private final float b;

    public SlopeAABB(float x, float y, float width, float height, float m, float b) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.m = m;
        this.b = b;
    }

    public SlopeAABB(Rectangle rectangle, float m, float b) {
        this.x = rectangle.x;
        this.y = rectangle.y;
        this.width = rectangle.width;
        this.height = rectangle.height;
        this.m = m;
        this.b = b;
    }

    /**
     * F(x) = y = m*x + b babyyyy!
     *
     * @param x X-coordinate
     * @return The height of the slope at coordinate x
     */
    public float f(float x) {
        return m * (x - this.x) + b + this.y;
    }

    /**
     * Slope getter.
     *
     * @return m
     */
    public float getSlope() {
        return m;
    }

    @Override
    public Class<? extends AABB> type() {
        return SlopeAABB.class;
    }
}
