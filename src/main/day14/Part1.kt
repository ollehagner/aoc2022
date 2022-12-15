package day14

import common.Direction
import common.Direction.*
import common.Grid
import common.Point
import common.Vector
import day14.CaveContent.*
import readInput
import java.util.function.Predicate

val SAND_SOURCE_POINT = Point(500,0)

fun main() {
    val cave = parseInput(readInput("day14/input.txt"))
    val yLimit = cave.max().y + 1
    do {
        var noOverflow = pourSand(cave) { point -> point.y > yLimit }
    } while(noOverflow)

    val restingSandUnits = cave.values().filter { it == SAND }.count()
    println("Day 14 part 1. Units of sand resting: $restingSandUnits")
}

fun parseInput(input: List<String>): Grid<CaveContent> {
    val rocks = input
        .map { row -> row.split(" -> ")
            .map { it.split(",").let { values ->
                Point(values[0].toInt(), values[1].toInt())
            } }
            .windowed(2, 1)
            .flatMap { (start, end) -> Vector(start, end).allPoints() }
        }.flatten()
    return Grid(rocks, ROCK)
}


fun pourSand(cave: Grid<CaveContent>, stopCondition: Predicate<Point>): Boolean {
    var sandUnitPosition = SAND_SOURCE_POINT
    var directionToMove: Direction
    do {
        if(stopCondition.test(sandUnitPosition)) return false
        directionToMove = listOf(UP, UP_LEFT, UP_RIGHT, NONE)
            .first { direction -> !cave.hasValue(sandUnitPosition.move(direction)) }
        sandUnitPosition = sandUnitPosition.move(directionToMove)
    } while(directionToMove != NONE)
    cave.set(sandUnitPosition, SAND)
    return true
}




enum class CaveContent(val symbol: Char) {
    ROCK('#'), AIR('.'), SAND('o'), SAND_SOURCE('+')
}