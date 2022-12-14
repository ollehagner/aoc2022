package common

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    constructor(values: String) :
            this(values.split(",").first().toInt(), values.split(",").last().toInt())

    companion object {
        fun sequence(start: Point,direction: Direction, steps: Int = 1): Sequence<Point> = sequence {
            var current = start
            while(true) {
                yield(current)
                current = direction.from(current, steps)
            }
        }
    }

    fun move(direction: Direction, steps: Int = 1): Point {
        return direction.from(this, steps)
    }

    fun move(vector: Point): Point {
        return Point(this.x + vector.x, this.y + vector.y)
    }

    fun move(x: Int, y: Int): Point {
        return Point(this.x + x, this.y + y)
    }

    fun relativeTo(other: Point): Point {
        return Point(other.x - this.x, other.y - this.y)
    }

    fun manhattanDistance(other: Point): Int {
        return abs(this.x - other.x) + abs(this.y - other.y)
    }

    override fun toString(): String {
        return "($x,$y)"
    }

}
