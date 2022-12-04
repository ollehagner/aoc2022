package day04

import overlap
import readInput

fun main() {
    val testinput = readInput("day04/testinput.txt")
    val input = readInput("day04/input.txt")

    val fullyOverlappingAssignments = input
        .map { toRanges(it) }
        .count { intRanges -> intRanges.first.overlap(intRanges.second) }
    println("Day 04 part 1. Num of fully overlapping assignments $fullyOverlappingAssignments")
}

