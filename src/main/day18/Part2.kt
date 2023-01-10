package day18

import common.Point3D
import readInput
import java.util.*

fun main() {
    val lavaDroplet = readInput("day18/input.txt")
        .map { it.split(",") }
        .map { (x, y, z) -> Point3D(x.toInt(), y.toInt(), z.toInt()) }

    val xLimits = IntRange(lavaDroplet.minOf { it.x } - 1, lavaDroplet.maxOf { it.x } + 1)
    val yLimits = IntRange(lavaDroplet.minOf { it.y } - 1, lavaDroplet.maxOf { it.y } + 1)
    val zLimits = IntRange(lavaDroplet.minOf { it.z } - 1, lavaDroplet.maxOf { it.z } + 1)

    val startingPoint = Point3D(xLimits.first, yLimits.first, zLimits.first)

    val visited = mutableSetOf<Point3D>()
    var toExplore = LinkedList<Point3D>()
    toExplore.add(startingPoint)
    var externalSides = 0

    while(toExplore.isNotEmpty()) {
        val current = toExplore.pop()

        if(!visited.contains(current)) {
            visited.add(current)
            current.adjacent()
                .filter { xLimits.contains(it.x) && yLimits.contains(it.y) && zLimits.contains(it.z) }
                .forEach {
                    if (lavaDroplet.contains(it)) {
                        externalSides++
                    } else {
                        toExplore.add(it)
                    }
                }
        }
    }

    println("Day 18 part 2. Total surface area $externalSides")
}