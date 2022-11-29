package common

import java.math.BigDecimal
import kotlin.math.sqrt

class Vector(val from: Point, val to: Point) {

    fun length(): BigDecimal {
        val horizontalSquared = BigDecimal(horizontalLength()).pow(2)
        val verticalSquared = BigDecimal(verticalLength()).pow(2)
        return sqrt((horizontalSquared + verticalSquared).toDouble()).toBigDecimal()
    }

    private fun horizontalLength(): Int {
        return to.x - from.x;
    }

    private fun verticalLength(): Int {
        return to.y - from.y
    }
}