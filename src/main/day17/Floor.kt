package day17

import common.Grid
import common.Point
import java.lang.IllegalStateException
import kotlin.math.abs

data class Floor(val points: Set<Point>) {

    constructor(grid: Grid<String>) : this(
        grid.allPoints()
            .groupBy { it.x }
            .values
            .map {
                column -> column.maxBy { it.y }
            }.toSet())

    fun normalized(): Floor {
        val minY = points.minOfOrNull { it.y } ?: throw IllegalStateException("Floor should not be empty")
        return Floor(points.map { it.copy(y = it.y - minY) }.toSet())
    }

    override fun toString(): String {
        return "Floor $points)"
    }

    companion object {
        fun start(): Floor {
            return Floor(IntRange(0, 6).map { x -> Point(x, 0) }.toSet())
        }
    }
}