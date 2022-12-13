package day12

import common.Direction.*
import common.Grid
import common.Point
import dequeOf
import readInput
import java.util.*

typealias Path = Deque<Point>

fun main() {
    val (start, goal, grid) = parseInput(readInput("day12/input.txt"))
    val shortestPath = shortestPath(start, goal, grid)
    println("Day 12 part 1. Fewest steps to goal: $shortestPath")
}

fun shortestPath(start: Point, goal: Point, grid: Grid<Int>): Int {
    val paths: Deque<Path> = dequeOf()
    paths.add(dequeOf(start))

    val pathCosts: MutableMap<Point, Int> = mutableMapOf()
    while(paths.isNotEmpty()) {
        val path = paths.pop()
        if(path.last != goal) {
            validMoves(path.last, grid)
                .map { nextPosition -> path.copyAndAdd(nextPosition) }
                .forEach { pathToExplore ->
                    if (pathToExplore.size < pathCosts.getOrDefault(pathToExplore.last(), Int.MAX_VALUE)) {
                        pathCosts[pathToExplore.last()] = pathToExplore.size
                        paths.add(pathToExplore)
                    }
                }
        }
    }
    return pathCosts.getOrDefault(goal, Int.MAX_VALUE) - 1
}

fun validMoves(currentPosition: Point, grid: Grid<Int>): List<Point> {

    return listOf(UP, DOWN, RIGHT, LEFT)
        .map { direction -> currentPosition.move(direction) }
        .filter { point -> grid.hasValue(point) && grid.valueOf(point) <= grid.valueOf(currentPosition) + 1}
}

fun Path.copyAndAdd(value: Point): Path {
    val copy = dequeOf<Point>()
    copy.addAll(this)
    copy.addLast(value)
    return copy
}


private const val START_CHAR = 'S'
private const val START_HEIGHT = 'a'.code

private const val GOAL_CHAR = 'E'
private const val GOAL_HEIGHT = 'z'.code

fun parseInput(input: List<String>): Triple<Point, Point, Grid<Int>> {
    var start = Point(0,0)
    var goal = Point(0,0)
    val grid = Grid<Int>()
    input.forEachIndexed { y, row ->
        row.forEachIndexed { x, heightChar ->
            val point = Point(x, y)
            if(heightChar == START_CHAR) {
                start = point
                grid.set(point, START_HEIGHT)
            } else if(heightChar == GOAL_CHAR) {
                goal = point
                grid.set(point, GOAL_HEIGHT)
            } else {
                grid.set(point, heightChar.code)
            }
        }
    }
    return Triple(start, goal, grid)

}
