package day09

import common.Direction
import common.Point
import java.util.*
import kotlin.math.abs

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
        val direction = when {
            diff.x == 0 && diff.y > 0 -> Direction.UP
            diff.x == 0 && diff.y < 0 -> Direction.DOWN
            diff.x > 0 && diff.y == 0 -> Direction.RIGHT
            diff.x < 0 && diff.y == 0 -> Direction.LEFT
            diff.x > 0 && diff.y > 0 -> Direction.UP_RIGHT
            diff.x > 0 && diff.y < 0 -> Direction.DOWN_RIGHT
            diff.x < 0 && diff.y > 0 -> Direction.UP_LEFT
            diff.x < 0 && diff.y < 0 -> Direction.DOWN_LEFT
            else -> Direction.NONE
        }
        if(abs(diff.x) > 1 || abs(diff.y) > 1) {
            moveTo(position().move(direction))
        }
    }
}