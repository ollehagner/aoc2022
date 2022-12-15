package day14

import common.Direction
import common.Grid
import common.Point
import readInput
import java.util.function.Predicate

fun main() {
    val cave = parseInput(readInput("day14/input.txt"))
    val yLimit = cave.max().y + 2
    do {
        val stopFunction = { point: Point -> cave.hasValue(SAND_SOURCE_POINT) }
        val possibleMoveFunction = { point: Point, cave: Grid<CaveContent> ->
            !cave.hasValue(point) && (point.y + 1) <= yLimit
        }
        var sandSourcePlugged = pourSand(cave, possibleMoveFunction, stopFunction)
    } while(sandSourcePlugged)

    val restingSandUnits = cave.values().filter { it == CaveContent.SAND }.count()
    println("Day 14 part 2. Units of sand resting: $restingSandUnits")
}

fun pourSand(cave: Grid<CaveContent>, possibleMove: (Point, Grid<CaveContent>) -> Boolean, stopCondition: Predicate<Point>): Boolean {
    var sandUnitPosition = SAND_SOURCE_POINT
    var directionToMove: Direction
    do {
        if(stopCondition.test(sandUnitPosition)) return false
        directionToMove = listOf(Direction.UP, Direction.UP_LEFT, Direction.UP_RIGHT, Direction.NONE)
            .first { direction -> possibleMove(sandUnitPosition.move(direction), cave) }
        sandUnitPosition = sandUnitPosition.move(directionToMove)
    } while(directionToMove != Direction.NONE)
    cave.set(sandUnitPosition, CaveContent.SAND)
    return true
}