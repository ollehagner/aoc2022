package day04

import readInput
import contains

fun main() {
    val testinput = readInput("day04/testinput.txt")
    val input = readInput("day04/input.txt")

    val fullyOverlappingAssignments = input
        .map { it.split(",") }
        .map { assignments -> assignments.map { toRange(it) } }
        .count { intRanges ->
            intRanges.first().contains(intRanges.last()) || intRanges.last().contains(intRanges.first())
        }
    println("Day 04 part 1. Num of fully overlapping assignments $fullyOverlappingAssignments")
}

fun toRange(assignment: String): IntRange {
    val components = assignment.split("-")
    return IntRange(components.first().toInt(), components.last().toInt())
}

