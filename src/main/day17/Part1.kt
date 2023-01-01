package day17

import LoopingIterator
import common.Direction
import common.Direction.*
import common.Grid
import common.Point
import groupUntil
import infiniteSequence
import readInput
import java.lang.IllegalArgumentException
import kotlin.math.abs

fun main() {
    val rocks = infiniteSequence(readInput("day17/rocks.txt")
        .groupUntil { it.isBlank() }
        .map { Rock.parse(it) })
    val jets = LoopingIterator(readInput("day17/input.txt").first().map { value -> if(value == '<') listOf(LEFT, DOWN) else listOf(RIGHT, DOWN)}.flatten())
    val startingFloor = Floor(IntRange(0,6).map { Point(it, 0) })
    val grid = Grid(startingFloor.points, "-")
    rocks
        .take(2022)
        .forEach { rock ->
            val droppedRock = dropRock(adjustToFloor(rock, grid.max().y), grid, jets)
            droppedRock.parts.forEach { grid.set(it, "#") }
        }

    println("Day 17 part 1. Highest point: ${grid.max().y}")
}

fun dropRock(rock: Rock, grid: Grid<String>, directions: LoopingIterator<Direction>): Rock {

    var movingRock = rock
    while(true) {
        val nextDirection = directions.next()
        if(nextDirection == DOWN && movingRock.move(DOWN, grid).parts.any { grid.hasValue(it) }) return movingRock
        movingRock = movingRock.move(nextDirection, grid)
    }
}

fun adjustToFloor(rock: Rock, maxY: Int): Rock {
    return Rock(rock.parts.map { (x, y) -> Point(x + 2, y + maxY + 4) })
}

class Rock(val parts: List<Point>) {

    private val leftBoundary = 0
    private val rightBoundary = 6

    companion object {
        fun parse(input: List<String>): Rock {
            val parts = input
                .flatMapIndexed() { index, line ->
                    val y = input.size - 1 - index
                    line.mapIndexedNotNull { x, value ->
                        if(value == '#') Point(x, y) else null
                    }
                }
            return Rock(parts)
        }
    }

    fun move(direction: Direction, grid: Grid<String>, steps: Int = 1): Rock {
        val movedRock = Rock(parts.map { it.move(direction, steps) })
        return when(direction) {
            LEFT -> {
                if(parts.minBy { it.x }.x == leftBoundary || movedRock.parts.any { grid.hasValue(it) }) this else movedRock
            }
            RIGHT -> {
                if(parts.maxBy { it.x }.x == rightBoundary || movedRock.parts.any { grid.hasValue(it) }) this else movedRock
            }
            DOWN -> {
                movedRock
            }
            UP -> {
                movedRock
            }
            else -> throw IllegalArgumentException("Illegal direction $direction")
        }
    }

    fun topLayer(): List<Point> {
        return parts
            .groupBy { it.x }
            .values
            .map { points -> points.maxBy { it.y } }
    }

    fun bottomLayer(): List<Point> {
        return parts
            .groupBy { it.x }
            .values
            .map { points -> points.minBy { it.y } }
    }

    fun leftMost(): List<Point> {
        return parts
            .groupBy { it.y }
            .values
            .map { points -> points.minBy { it.x } }
    }

    fun rightMost(): List<Point> {
        return parts
            .groupBy { it.y }
            .values
            .map { points -> points.maxBy { it.x } }
    }

    override fun toString(): String {
        return Grid(parts, "#").toStringInvertedVertical { it }
    }


}

data class Floor(val points: List<Point>) {

    fun touches(rock: Rock): Boolean {
        return listOf(points, rock.bottomLayer()).flatten()
            .groupBy { it.x }
            .values
            .filter { it.size == 2 }
            .any { abs(it.first().y - it.last().y) == 1 }
    }

    fun addRock(rock: Rock): Floor  {
        return Floor(listOf(points, rock.topLayer()).flatten()
            .groupBy { it.x }
            .values
            .map { it.maxBy { point -> point.y } })
    }

    fun highestPoint(): Int {
        return points.maxOf { it.y }
    }

    override fun toString(): String {
        return "Floor $points)"
    }

    companion object {
        fun start(): Floor {
            return Floor(IntRange(0, 6).map { x -> Point(x, 0) })
        }
    }
}

