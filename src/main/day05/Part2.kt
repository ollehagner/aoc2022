package day05

import readInput

fun main() {
//        val input = readInput("day05/testinput.txt")
    val input = readInput("day05/input.txt")
    val stacks = Stacks(input)
    input
        .dropWhile { it.isNotBlank() }
        .filter { it.isNotBlank() }
        .map { Move(it) }
        .forEach { move -> stacks.executeMultipleMove(move) }
    println("Day 5 part 2. Top of stacks:" + stacks.topOfEachStack())
}