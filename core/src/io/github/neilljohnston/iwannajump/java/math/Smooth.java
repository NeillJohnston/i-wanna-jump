package io.github.neilljohnston.iwannajump.java.math;

/**
 * Mathematical smoothing and interpolation functions.
 */
public class Smooth {
    /**
     * Bring x to a proportion of t between x and y.
     * 
     * @param x Starting number
     * @param y Target number
     * @param t Time
     * @return Interpolated value for x
     */
    public static float lerp(float x, float y, float t) {
        return x + (y - x) * t;
    }

    /**
     * Bring x to a proportion of t between x and y.
     *
     * @param x Starting number
     * @param y Target number
     * @param t Time
     * @return Interpolated value for x
     */
    public static double lerp(double x, double y, double t) {
        return x + (y - x) * t;
    }

    /**
     * A sin function with period of 2 and amplitude of 1.
     *
     * @param x A number
     * @return sin(one divided by two multiplied by pi multiplied by x)
     */
    public static float unitSin(float x) {
        return (float) Math.sin(Math.PI / 2 * x);
    }

    /**
     * A sin function with period of 2 and amplitude of 1.
     *
     * @param x A number
     * @return sin(one divided by two multiplied by pi multiplied by x)
     */
    public static double unitSin(double x) {
        return Math.sin(Math.PI / 2 * x);
    }
}
