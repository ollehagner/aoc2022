package day17

import LoopingIterator
import common.Direction
import common.Grid
import common.Point
import groupUntil
import infiniteSequence
import readInput
import java.lang.IllegalArgumentException

class Rock(val id: Int, val parts: List<Point>) {

    private val leftBoundary = 0
    private val rightBoundary = 6

    companion object {
        fun rockSequence(): Sequence<Rock> {
            return infiniteSequence(readInput("day17/rocks.txt")
                .groupUntil { it.isBlank() }
                .mapIndexed { index, lines -> parse(index, lines) })
        }

        fun rockIterator(): LoopingIterator<Rock> {
            return LoopingIterator(readInput("day17/rocks.txt")
                .groupUntil { it.isBlank() }
                .mapIndexed { index, lines -> parse(index, lines) })
        }

        fun parse(id: Int, input: List<String>): Rock {
            val parts = input
                .flatMapIndexed() { index, line ->
                    val y = input.size - 1 - index
                    line.mapIndexedNotNull { x, value ->
                        if(value == '#') Point(x, y) else null
                    }
                }
            return Rock(id, parts)
        }
    }

    fun move(direction: Direction, grid: Grid<String>, steps: Int = 1): Rock {
        val movedRock = Rock(id, parts.map { it.move(direction, steps) })
        return when(direction) {
            Direction.LEFT -> {
                if(parts.minBy { it.x }.x == leftBoundary || movedRock.parts.any { grid.hasValue(it) }) this else movedRock
            }
            Direction.RIGHT -> {
                if(parts.maxBy { it.x }.x == rightBoundary || movedRock.parts.any { grid.hasValue(it) }) this else movedRock
            }
            Direction.DOWN -> {
                movedRock
            }
            Direction.UP -> {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rock

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}