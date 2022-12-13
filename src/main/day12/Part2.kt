package day12

import readInput

fun main() {
    val (start, goal, grid) = parseInput(readInput("day12/input.txt"))
    val possibleStarts = grid.entries()
        .filter { it.value == 'a'.code }
        .map { it.key }

    val shortestPath = possibleStarts.minOfOrNull {
        shortestPath(it, goal, grid)
    }
    println("Day 12 part 2. Shortest path: $shortestPath")
}