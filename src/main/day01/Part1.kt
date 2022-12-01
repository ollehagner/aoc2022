package day01

import readInput

fun main() {

    val testInput = readInput("day01/testinput.txt")
    val input = readInput("day01/input.txt")

    val maxCalories = input
        .map { if(it.isEmpty()) 0 else it.toInt() }
        .scan(0) { acc, value -> if(value == 0) 0 else acc + value }
        .max()
    println("Part 1 max calories: $maxCalories")
}