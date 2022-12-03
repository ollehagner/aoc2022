package day01

import groupUntil
import readInput;

fun main() {

    val testInput = readInput("day01/testinput.txt")
    val input = readInput("day01/input.txt")

    val topThreeTotalCalories = input
        .groupUntil { it.isEmpty() }
        .map { it.sumOf { value -> if(value.isEmpty()) 0 else value.toInt() } }
        .sortedDescending()
        .take(3)
        .sum()

    println("Day 01 part 2 total top three calories: $topThreeTotalCalories")
}