package io.github.neilljohnston.iwannajump.java.math;

/**
 * Basic extended math operations.
 */
public class ExMath {
    private long a;

    /**
     * Return x, bounded by lower and upper.
     *
     * @param x     A number
     * @param lower Lower bound of x
     * @param upper Upper bound of x
     * @return x if x is within [lower, upper], otherwise the bound that x escaped
     */
    public static int bounded(int x, int lower, int upper) {
        return Math.min(Math.max(x, lower), upper);
    }

    /**
     * Return x, bounded by lower and upper.
     *
     * @param x     A number
     * @param lower Lower bound of x
     * @param upper Upper bound of x
     * @return x if x is within [lower, upper], otherwise the bound that x escaped
     */
    public static long bounded(long x, long lower, long upper) {
        return Math.min(Math.max(x, lower), upper);
    }

    /**
     * Return x, bounded by lower and upper.
     *
     * @param x     A number
     * @param lower Lower bound of x
     * @param upper Upper bound of x
     * @return x if x is within [lower, upper], otherwise the bound that x escaped
     */
    public static float bounded(float x, float lower, float upper) {
        return Math.min(Math.max(x, lower), upper);
    }

    /**
     * Return x, bounded by lower and upper.
     *
     * @param x     A number
     * @param lower Lower bound of x
     * @param upper Upper bound of x
     * @return x if x is within [lower, upper], otherwise the bound that x escaped
     */
    public static double bounded(double x, double lower, double upper) {
        return Math.min(Math.max(x, lower), upper);
    }

    /**
     * Return the number with the greatest absolute value. Will keep the sign.
     * 
     * @param a A number
     * @param b A number
     * @return Either a or b, whichever has the greatest absolute value
     */
    public static int absMax(int a, int b) {
        return Math.max(Math.abs(a), Math.abs(b)) == Math.abs(a) ? a : b;
    }

    /**
     * Return the number with the greatest absolute value. Will keep the sign.
     *
     * @param a A number
     * @param b A number
     * @return Either a or b, whichever has the greatest absolute value
     */
    public static long absMax(long a, long b) {
        return Math.max(Math.abs(a), Math.abs(b)) == Math.abs(a) ? a : b;
    }

    /**
     * Return the number with the greatest absolute value. Will keep the sign.
     *
     * @param a A number
     * @param b A number
     * @return Either a or b, whichever has the greatest absolute value
     */
    public static float absMax(float a, float b) {
        return Math.max(Math.abs(a), Math.abs(b)) == Math.abs(a) ? a : b;
    }

    /**
     * Return the number with the greatest absolute value. Will keep the sign.
     *
     * @param a A number
     * @param b A number
     * @return Either a or b, whichever has the greatest absolute value
     */
    public static double absMax(double a, double b) {
        return Math.max(Math.abs(a), Math.abs(b)) == Math.abs(a) ? a : b;
    }

    /**
     * Return the number with the least absolute value. Will keep the sign.
     *
     * @param a A number
     * @param b A number
     * @return Either a or b, whichever has the least absolute value
     */
    public static int absMin(int a, int b) {
        return Math.min(Math.abs(a), Math.abs(b)) == Math.abs(a) ? a : b;
    }

    /**
     * Return the number with the least absolute value. Will keep the sign.
     *
     * @param a A number
     * @param b A number
     * @return Either a or b, whichever has the least absolute value
     */
    public static long absMin(long a, long b) {
        return Math.min(Math.abs(a), Math.abs(b)) == Math.abs(a) ? a : b;
    }

    /**
     * Return the number with the least absolute value. Will keep the sign.
     *
     * @param a A number
     * @param b A number
     * @return Either a or b, whichever has the least absolute value
     */
    public static float absMin(float a, float b) {
        return Math.min(Math.abs(a), Math.abs(b)) == Math.abs(a) ? a : b;
    }

    /**
     * Return the number with the least absolute value. Will keep the sign.
     *
     * @param a A number
     * @param b A number
     * @return Either a or b, whichever has the least absolute value
     */
    public static double absMin(double a, double b) {
        return Math.min(Math.abs(a), Math.abs(b)) == Math.abs(a) ? a : b;
    }

    /**
     * Allow x to follow the behavior of a piecewise function.
     * Keys determine the minimum threshold for the function to pass to the next value.
     * The length of values should always be one more than the length of keys.
     *
     * For instance, calling piecewise(x, keys, values) if keys = [2, 4, 8] and values = [10, 20, 30, 40]:
     * -    When x < 2 return 10,
     * -    2 <= x < 4 return 20,
     * -    4 <= x < 8 return 30,
     * -    and 8 <= x return 40.
     *
     * @param x         Test value
     * @param keys      An array of values (in ascending order) that x can take on
     * @param values    An array of values that can be returned
     * @return The matching value for the range that x lies in
     */
    public static int piecewise(int x, int[] keys, int[] values) {
        for(int i = 0; i < keys.length; i++)
            if(x < keys[i])
                return values[i];
        return values[values.length - 1];
    }

    /**
     * Allow x to follow the behavior of a piecewise function.
     * Keys determine the minimum threshold for the function to pass to the next value.
     * The length of values should always be one more than the length of keys.
     *
     * For instance, calling piecewise(x, keys, values) if keys = [2, 4, 8] and values = [10, 20, 30, 40]:
     * -    When x < 2 return 10,
     * -    2 <= x < 4 return 20,
     * -    4 <= x < 8 return 30,
     * -    and 8 <= x return 40.
     *
     * @param x         Test value
     * @param keys      An array of values (in ascending order) that x can take on
     * @param values    An array of values that can be returned
     * @return The matching value for the range that x lies in
     */
    public static float piecewise(float x, float[] keys, float[] values) {
        for(int i = 0; i < keys.length; i++)
            if(x < keys[i])
                return values[i];
        return values[values.length - 1];
    }
}
