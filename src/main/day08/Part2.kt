package day08

import common.Direction
import common.Grid
import common.Point
import readInput

fun main() {
    val input = readInput("day08/input.txt")
    val grid = Grid(input.map { row -> row.chunked(1).map { it.toInt() } })
    val maxView = grid.allPoints()
        .maxOfOrNull { grid.allviews(it) }

    println("Day 8 part 2 max view value: $maxView")

}

fun Grid<Int>.allviews(position: Point): Int {
    return listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
        .map { direction ->  view(position, direction) }
        .reduce { a, b -> a * b}
}


fun Grid<Int>.view(position: Point, direction: Direction): Int {
    if(!hasValue(position.move(direction))) {
        return 0;
    }
    val height = valueOf(position)
    return generateSequence(position) { newPosition -> newPosition.move(direction) }
        .drop(1)
        .takeWhile { hasValue(it) }
        .fold(Pair(0, true)){ countAndContinue, point ->
            if(countAndContinue.second) {
                Pair(countAndContinue.first + 1, valueOf(point) < height)
            } else {
                countAndContinue
            }
        }.first

}