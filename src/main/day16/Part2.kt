package day16

import readInput

fun main() {
    val start = System.currentTimeMillis()
    val valves = readInput("day16/input.txt").map { Valve.parse(it) }
    val distances = calculateDistances(valves)

    val openers = listOf(ValveOpener("You", "AA"), ValveOpener("Elephant","AA"))
    val bestpath = solve(State(openers, valves.filter { it.flowRate > 0 }), distances, 26)
    println("Day 16 part 2. Best path pressure release: ${bestpath.releasedPressure(26)}. Finished in ${System.currentTimeMillis() - start} ms")
}

