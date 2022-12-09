package day09

import common.Point

import readInput

fun main() {
    val startingPoint = Point(0, 0)
    val knots = List(10) { Knot(startingPoint)}
    val moves = parseInput(readInput("day09/input.txt"))
    moves.forEach { headDirection ->
        knots.first().move(headDirection)
        knots
            .windowed(2)
            .forEach { (leader, follower) ->
                follower.follow(leader)
            }
    }
    println("Day 09 part 2, unique tail positions ${knots.last().path().toSet().size}")
}