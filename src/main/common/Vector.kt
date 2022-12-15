package common

import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.sqrt


class Vector(private val from: Point, val to: Point) {

    fun magnitude(): Double {
        val horizontalSquared = BigDecimal(horizontalDiff()).pow(2)
        val verticalSquared = BigDecimal(verticalDiff()).pow(2)
        return sqrt((horizontalSquared + verticalSquared).toDouble())
    }

    fun add(other: Vector): Vector {
        return Vector(from, Point(horizontalDiff() + other.horizontalDiff(), verticalDiff() + other.verticalDiff()))
    }

    fun horizontalDiff(): Int {
        return to.x - from.x;
    }

    fun verticalDiff(): Int {
        return to.y - from.y
    }

    private fun minX(): Int {
        return minOf(to.x, from.x)
    }

    private fun maxX(): Int {
        return maxOf(to.x, from.x)
    }

    private fun minY(): Int {
        return minOf(to.y, from.y)
    }

    private fun maxY(): Int {
        return maxOf(to.y, from.y)
    }

    fun allPoints(): List<Point> {
        return IntRange(minX(), maxX()).flatMap { x ->
            IntRange(minY(), maxY()).map {y ->
                Point(x, y)
            }
        }
    }
}