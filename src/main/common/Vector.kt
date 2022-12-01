package common

import java.math.BigDecimal
import kotlin.math.sqrt

class Vector(val from: Point, val to: Point) {

    fun magnitude(): Double {
        val horizontalSquared = BigDecimal(horizontal()).pow(2)
        val verticalSquared = BigDecimal(vertical()).pow(2)
        return sqrt((horizontalSquared + verticalSquared).toDouble())
    }

    fun add(other: Vector): Vector {
        return Vector(from, Point(horizontal() + other.horizontal(), vertical() + other.vertical()))
    }

    fun horizontal(): Int {
        return to.x - from.x;
    }

    fun vertical(): Int {
        return to.y - from.y
    }
}