package day18

import common.Point3D
import readInput

fun main() {
    val lavaDroplet = readInput("day18/input.txt")
        .map { it.split(",") }
        .map { (x, y, z) -> Point3D(x.toInt(), y.toInt(), z.toInt()) }

    val surfaceArea = lavaDroplet.sumOf { part ->
        part.adjacent().count { !lavaDroplet.contains(it) }
    }

    println("Day 18 part 1. Total surface area: $surfaceArea")
}