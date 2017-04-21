package io.neilljohnston.github.exmath

trait Smooth {
    def bungeeSin(x: Float): Float = math.sin(x * math.Pi / 2).toFloat

    def smoothTo(a: Float, b: Float, p: Float): Float = (b - a) * p

    def accelWithMax(a: Float, b: Float, step: Float): Float = math.min(a + step, b)
}
