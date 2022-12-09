package day09

import common.Direction
import common.Point
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

class Knot(start: Point) {
    private val path: LinkedList<Point> = LinkedList(listOf(start))

    fun position(): Point {
        return path.first
    }

    fun move(direction: Direction) {
        path.push(position().move(direction))
    }

    private fun moveTo(point: Point) {
        path.push(point)
    }

    fun path(): List<Point> {
        return path.toList()
    }

    fun follow(other: Knot) {
        val otherPosition = other.position()
        val diff = position().relativeTo(otherPosition)
        if(abs(diff.x) > 1 || abs(diff.y) > 1) {
            moveTo(position().move(diff.x.sign, diff.y.sign))
        }
    }
}