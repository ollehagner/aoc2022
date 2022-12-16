package day15

import common.Point
import readInput

fun main() {
    val sensors = parseInput(readInput("day15/input.txt"))
    val searchArea = 0..4000000
    val distressBeaconPosition = searchArea
        .map { y -> Pair(y, coveredRowPositions(sensors, y)) }
        .filter { (_, coveredPositionRanges) -> coveredPositionRanges.none { it.fullyOverlaps(searchArea) } }
        .map { (y, ranges) -> Point(ranges.minOf { it.last } + 1, y) }
        .first()

    val tuningFrequency = (distressBeaconPosition.x.toLong() * 4000000) + distressBeaconPosition.y.toLong()
    println("Day 15 part 2. Distress beacon tuning frequency: $tuningFrequency")

}
    private infix fun IntRange.fullyOverlaps(other: IntRange): Boolean =
        first <= other.first && last >= other.last