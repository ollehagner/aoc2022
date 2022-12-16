package day15

import common.Point
import readInput
import kotlin.math.abs

fun main() {
    val sensors = parseInput(readInput("day15/input.txt"))
    val rowToCheck = 2000000
    val beaconsInRow = sensors.map { it.beacon }.filter { it.y == rowToCheck }.distinct().count()
    val positionsWithNoBeacon = coveredRowPositions(sensors, rowToCheck)
        .sumOf { it.count() } - beaconsInRow

    println("Day 15 part 1. Positions with no beacons: $positionsWithNoBeacon")
}

fun coveredRowPositions(sensors: List<Sensor>, rowToCheck: Int): List<IntRange> {

    return sensors
        .map { it.rowCoverage(rowToCheck) }
        .filterNotNull()
        .fold(listOf()) { acc, rowCoverage ->
            merge(buildList {
                addAll(acc)
                add(rowCoverage)
            })
        }
}

fun merge(ranges: List<IntRange>): List<IntRange> {
    if(ranges.size == 1) return ranges
    val toCheck = ranges.first()
    val overlapping = ranges.drop(1).filter { it.overlapsOrAdjoins(toCheck) }
    val notOverlapping = ranges.drop(1).filter { !it.overlapsOrAdjoins(toCheck) }
    return if(overlapping.isEmpty()) {
        listOf(listOf(toCheck), merge(ranges.drop(1))).flatten()
    } else {
        val merged = overlapping.fold(toCheck) { acc, value -> acc.merge(value) }
        return merge(listOf(listOf(merged), notOverlapping).flatten())
    }
}

fun parseInput(input: List<String>): List<Sensor> {
    val pattern = Regex("""Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""")
    return input
        .map{ pattern.find(it)!!.destructured }
        .map { (sensorX, sensorY, beaconX, beaconY) -> Sensor(Point(sensorX.toInt(), sensorY.toInt()), Point(beaconX.toInt(), beaconY.toInt())) }
}

data class Sensor(val position: Point, val beacon: Point) {

    fun rowCoverage(yValue: Int): IntRange? {
        val horizontalReach = position.manhattanDistance(beacon) - abs(position.y - yValue)
        return if(horizontalReach > 0) {
            IntRange(position.x - horizontalReach, position.x + horizontalReach)
        } else {
            null
        }
    }
}

private infix fun IntRange.overlapsOrAdjoins(other: IntRange): Boolean =
    (first <= other.last && other.first <= last) || last + 1 == other.first || other.last + 1 == first

private infix fun IntRange.merge(other: IntRange): IntRange {
    return IntRange(minOf(first, other.first), maxOf(last, other.last))
}