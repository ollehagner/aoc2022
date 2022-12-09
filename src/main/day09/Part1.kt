package day09

import common.Direction
import common.Point

import readInput
import java.lang.IllegalArgumentException

fun main() {
    val head = Knot(Point(0, 0))
    val tail = Knot(Point(0, 0))
    val moves = parseInput(readInput("day09/input.txt"))
    moves.forEach { headDirection ->
        head.move(headDirection)
        tail.follow(head)
    }
    println("Day 09 part 1, unique tail positions ${tail.path().toSet().size}")
}

fun parseInput(input: List<String>): List<Direction> {
    return input
        .flatMap {
            it.split(" ").let { (where, count) ->
                val direction = when (where) {
                    "D" -> Direction.DOWN
                    "U" -> Direction.UP
                    "L" -> Direction.LEFT
                    "R" -> Direction.RIGHT
                    else -> throw IllegalArgumentException("Unknown direction $where")
                }
                List(count.toInt()) { direction }
            }
        }
}

