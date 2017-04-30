package io.github.neilljohnston.iwannajump.exmath

/**
  * Smoothing functions galore.
  * Huge thanks to Desmos Graphing Calculator for existing.
  */
trait Smooth {
    /**
      * Sin transformation, bungeeSin(0) = 0 and bungeeSin(1) = 1
      * @param x    A value
      * @return sin(x*pi/2)
      */
    def bungeeSin(x: Float): Float = math.sin(x * math.Pi / 2).toFloat

    /**
      * Linear interpolation.
      * @param a    Starting value
      * @param b    Ending value
      * @param t    Increment
      * @return t/1 between a and b
      */
    def lerp(a: Float, b: Float, t: Float): Float = (b - a) * t + a
}
