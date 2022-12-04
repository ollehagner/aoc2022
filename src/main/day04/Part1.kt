package day04

import readInput
import fullyOverlaps

fun main() {
    val testinput = readInput("day04/testinput.txt")
    val input = readInput("day04/input.txt")

    val fullyOverlappingAssignments = input
        .map { toRanges(it) }
        .count { intRanges ->
            intRanges.first.fullyOverlaps(intRanges.second) || intRanges.second.fullyOverlaps(intRanges.first)
        }
    println("Day 04 part 1. Num of fully overlapping assignments $fullyOverlappingAssignments")
}

fun toRanges(assignmenta: String): Pair<IntRange, IntRange> {
    val pattern = Regex("""(\d+)-(\d+),(\d+)-(\d+)""")
    val (firstFrom, firstTo, secondFrom, secondTo) = pattern.find(assignmenta)!!.destructured
    return IntRange(firstFrom.toInt(), firstTo.toInt()) to IntRange(secondFrom.toInt(), secondTo.toInt())
}

